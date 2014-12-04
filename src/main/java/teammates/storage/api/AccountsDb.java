package teammates.storage.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Query;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import teammates.common.datatransfer.AccountAttributes;
import teammates.common.datatransfer.EntityAttributes;
import teammates.common.datatransfer.StudentProfileAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.ThreadHelper;
import teammates.common.util.Utils;
import teammates.storage.entity.Account;
import teammates.storage.entity.StudentProfile;

/**
 * Manejar operaciones CRUD para las cuentas.
 * La API utiliza clases de transferencia de datos (es decir, * Atributos) en lugar de las clases con persistencia.
 * 
 */
public class AccountsDb extends EntitiesDb {
    @SuppressWarnings("unused")
    private static final Logger log = Utils.getLogger();
    
    /**
     * Precondiciones: 
     * <br> * {@code accountToAdd} no es nulo y tiene datos válidos.
     */
    public void createAccount(AccountAttributes accountToAdd) 
            throws InvalidParametersException {
        // TODO: utilizar createEntity una vez haya una forma correcta de agregar cuentas de instructor.
        try {
            // esto es para código heredado a manipular
            if (accountToAdd != null && accountToAdd.studentProfile == null) {
                accountToAdd.studentProfile = new StudentProfileAttributes();
                accountToAdd.studentProfile.googleId = accountToAdd.googleId;
            }
            createEntity(accountToAdd);
        } catch (EntityAlreadyExistsException e) {
            // Actualizamos la cuenta una vez si ya existe. Esto es debido a cómo
            // la adición de las cuentas de instructor de trabajo.
            try {
                updateAccount(accountToAdd, true);
            } catch (EntityDoesNotExistException edne) {
                // Esta situación no se ha probado como la replicación de tal situación 
                // es difícil durante la prueba
                Assumption.fail("Entity found be already existing and not existing simultaneously");
            }
        }
    }
    
    /* Esta función se utiliza para persistir paquete de datos en proceso de prueba */
    public void createAccounts(Collection<AccountAttributes> accountsToAdd, boolean updateAccount) throws InvalidParametersException{
        
        List<EntityAttributes> accountsToUpdate = createEntities(accountsToAdd);
        if(updateAccount){
            for(EntityAttributes entity : accountsToUpdate){
                AccountAttributes account = (AccountAttributes) entity;
                try {
                    updateAccount(account, true);
                } catch (EntityDoesNotExistException e) {
                 // Esta situación no se ha probado como la replicación de tal situación 
                 // es difícil durante la prueba
                    Assumption.fail("Entity found be already existing and not existing simultaneously");
                }
            }
        }
    }
    
    /**
     * Obtiene la versión de la transferencia de datos de la cuenta. No recuperar el perfil
     * si el parámetro dado es falsa<br>
     * Precondiciones: 
     * <br> * Todos los parámetros son no nulo. 
     * @return Null si no se encontro.
     */
    public AccountAttributes getAccount(String googleId, boolean retrieveStudentProfile) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, googleId);
        
        Account a = getAccountEntity(googleId, retrieveStudentProfile);
    
        if (a == null) {
            return null;
        }
        closePM();
        
        AccountAttributes accAttr = new AccountAttributes(a);
        return accAttr;
    }
    
    public AccountAttributes getAccount(String googleId) {
        return getAccount(googleId, false);
    }

    /**
     * @return {@link AccountAttribute} objetos para todas las cuentas con privilegios de instructor.
     *   Devuelve una lista vacía si no se encuentran este tipo de cuentas.
     */
    public List<AccountAttributes> getInstructorAccounts() {
        Query q = getPM().newQuery(Account.class);
        q.setFilter("isInstructor == true");
        
        @SuppressWarnings("unchecked")
        List<Account> accountsList = (List<Account>) q.execute();
        
        List<AccountAttributes> instructorsAccountData = new ArrayList<AccountAttributes>();
                
        for (Account a : accountsList) {
            instructorsAccountData.add(new AccountAttributes(a));
        }
        
        return instructorsAccountData;
    }

    /**
     * Precondiciones: 
     * <br> * {@code accountToAdd} no es nulo y tiene datos válidos.
     */
    public void updateAccount(AccountAttributes a, boolean updateStudentProfile) 
            throws InvalidParametersException, EntityDoesNotExistException {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, a);
        
        if (!a.isValid()) {
            throw new InvalidParametersException(a.getInvalidityInfo());
        }
        
        Account accountToUpdate = getAccountEntity(a.googleId, updateStudentProfile);

        if (accountToUpdate == null) {
            throw new EntityDoesNotExistException(ERROR_UPDATE_NON_EXISTENT_ACCOUNT + a.googleId
                + ThreadHelper.getCurrentThreadStack());
        }
        
        a.sanitizeForSaving();
        accountToUpdate.setName(a.name);
        accountToUpdate.setEmail(a.email);
        accountToUpdate.setIsInstructor(a.isInstructor);
        accountToUpdate.setInstitute(a.institute);
        
        if (updateStudentProfile) {
            StudentProfileAttributes existingProfile = new StudentProfileAttributes(accountToUpdate.getStudentProfile());
            a.studentProfile.modifiedDate = existingProfile.modifiedDate;
            
            // if the student profile has changed then update the store
            // this is to maintain integrity of the modified date.
            if(!(existingProfile.toString().equals(a.studentProfile.toString()))) {
                accountToUpdate.setStudentProfile((StudentProfile) a.studentProfile.toEntity());
            }
        }
        
        closePM();
    }
    
    public void updateAccount(AccountAttributes a) 
            throws InvalidParametersException, EntityDoesNotExistException {
        if (a != null && a.studentProfile == null) {
            a.studentProfile = new StudentProfileAttributes();
            a.studentProfile.googleId = a.googleId;
        }
        updateAccount(a, false);
    }

    /**
     * Note: Esto no es una eliminación en cascada. <br>
     *   <br> Falla silenciosamente si no hay tal cuenta.
     * <br> Precondiciones: 
     * <br> * {@code googleId} no es null.
     */
    public void deleteAccount(String googleId) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, googleId);
        
        AccountAttributes accountToDelete = getAccount(googleId, true);

        if (accountToDelete == null) {
            return;
        }
        
        if (!accountToDelete.studentProfile.pictureKey.equals("")) {
            deletePicture(new BlobKey(accountToDelete.studentProfile.pictureKey));
        }
        deleteEntity(accountToDelete);
        closePM();
    }
    
    public void deleteAccounts(Collection<AccountAttributes> accounts){

        for(AccountAttributes accountToDelete : accounts){
            if (!accountToDelete.studentProfile.pictureKey.equals("")) {
                deletePicture(new BlobKey(accountToDelete.studentProfile.pictureKey));
            }
        }
        deleteEntities(accounts);
        closePM();
    }

    private Account getAccountEntity(String googleId, boolean retrieveStudentProfile) {
        
        try {
            Key key = KeyFactory.createKey(Account.class.getSimpleName(), googleId);
            Account account = getPM().getObjectById(Account.class, key);
            
            if (JDOHelper.isDeleted(account)) {
                return null;
            } else if (retrieveStudentProfile) {
                if (account.getStudentProfile() == null) {
                    // Esta situación no puede ser reproducido y, por tanto, no han sido evaluados
                    // Esto sólo ocurre cuando los datos existentes en el almacén no tienen un perfil
                    account.setStudentProfile(new StudentProfile(account.getGoogleId()));
                }
            }
            
            return account;
        } catch (IllegalArgumentException iae){
            return null;            
        } catch(JDOObjectNotFoundException je) {
            return null;
        }
    }
    
    private Account getAccountEntity(String googleId) {
        return getAccountEntity(googleId, false);
    }

    @Override
    protected Object getEntity(EntityAttributes entity) {
        return getAccountEntity(((AccountAttributes)entity).googleId);
    }
}

