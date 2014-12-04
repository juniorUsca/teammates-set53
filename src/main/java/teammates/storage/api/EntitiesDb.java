package teammates.storage.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchQueryException;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreFailureException;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import teammates.common.datatransfer.EntityAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Config;
import teammates.common.util.Const;
import teammates.common.util.ThreadHelper;
import teammates.common.util.Utils;
import teammates.storage.datastore.Datastore;
import teammates.storage.search.SearchDocument;
import teammates.storage.search.SearchManager;
import teammates.storage.search.SearchQuery;

public abstract class EntitiesDb {

    public static final String ERROR_CREATE_ENTITY_ALREADY_EXISTS = "Trying to create a %s that exists: ";
    public static final String ERROR_UPDATE_NON_EXISTENT = "Trying to update non-existent Entity: ";
    public static final String ERROR_UPDATE_NON_EXISTENT_ACCOUNT = "Trying to update non-existent Account: ";
    public static final String ERROR_UPDATE_NON_EXISTENT_STUDENT = "Trying to update non-existent Student: ";
    public static final String ERROR_UPDATE_NON_EXISTENT_STUDENT_PROFILE = "Trying to update non-existent Student Profile: ";
    public static final String ERROR_UPDATE_NON_EXISTENT_COURSE = "Trying to update non-existent Course: ";
    public static final String ERROR_UPDATE_NON_EXISTENT_INSTRUCTOR_PERMISSION = "Trying to update non-existing InstructorPermission: ";
    public static final String ERROR_UPDATE_TO_EXISTENT_INTRUCTOR_PERMISSION = "Trying to update to existent IntructorPermission: ";
    public static final String ERROR_CREATE_INSTRUCTOR_ALREADY_EXISTS = "Trying to create a Instructor that exists: ";
    public static final String ERROR_TRYING_TO_MAKE_NON_EXISTENT_ACCOUNT_AN_INSTRUCTOR = "Trying to make an non-existent account an Instructor :";

    private static final Logger log = Utils.getLogger();
    
    /**
     * Precondiciones: 
     * <br> * {@code entityToAdd} no es nulo y tiene datos válidos.
     */
    public Object createEntity(EntityAttributes entityToAdd) 
            throws InvalidParametersException, EntityAlreadyExistsException {
        
        Assumption.assertNotNull(
                Const.StatusCodes.DBLEVEL_NULL_INPUT, entityToAdd);
        
        entityToAdd.sanitizeForSaving();
        
        if (!entityToAdd.isValid()) {
            throw new InvalidParametersException(entityToAdd.getInvalidityInfo());
        }
        
        // TODO: ¿Realmente necesitamos identificadores especiales? Podemos solo usar ToString()? 
        // Respuesta: si. Nosostros podemos usar toString.
        if (getEntity(entityToAdd) != null) {
            String error = String.format(ERROR_CREATE_ENTITY_ALREADY_EXISTS, entityToAdd.getEntityTypeAsString())
                    + entityToAdd.getIdentificationString();
            log.info(error);
            throw new EntityAlreadyExistsException(error);
        }
        
        Object entity = entityToAdd.toEntity();
        getPM().makePersistent(entity);
        getPM().flush();

        // Espere a que la operación persista
        int elapsedTime = 0;
        Object createdEntity = getEntity(entityToAdd);
        if(Config.PERSISTENCE_CHECK_DURATION > 0){
            while ((createdEntity == null)
                    && (elapsedTime < Config.PERSISTENCE_CHECK_DURATION)) {
                ThreadHelper.waitBriefly();
                createdEntity = getEntity(entityToAdd);
                //comprobar antes de incrementar para evitar problema caso límite
                if (createdEntity == null) {
                    elapsedTime += ThreadHelper.WAIT_DURATION;
                }
            }
            if (elapsedTime == Config.PERSISTENCE_CHECK_DURATION) {
                log.severe("Operation did not persist in time: create"
                        + entityToAdd.getEntityTypeAsString() + "->"
                        + entityToAdd.getIdentificationString());
            }
        }
        return createdEntity;
    }
    
    public List<EntityAttributes> createEntities(Collection<? extends EntityAttributes> entitiesToAdd) throws InvalidParametersException {
        
        Assumption.assertNotNull(
                Const.StatusCodes.DBLEVEL_NULL_INPUT, entitiesToAdd);
        
        List<EntityAttributes> entitiesToUpdate = new ArrayList<EntityAttributes>();
        List<Object> entities = new ArrayList<Object>(); 
        
        for(EntityAttributes entityToAdd : entitiesToAdd){
            entityToAdd.sanitizeForSaving();
            
            if (!entityToAdd.isValid()) {
                throw new InvalidParametersException(entityToAdd.getInvalidityInfo());
            }
            
            if(getEntity(entityToAdd) != null){
                entitiesToUpdate.add(entityToAdd);
            } else {
                entities.add(entityToAdd.toEntity());
            }
        }
        
        getPM().makePersistentAll(entities);
        getPM().flush();
 
        return entitiesToUpdate;
    }

    
    /**
     * Advertencia: No utilice este método a menos que una actualización anterior podría causar 
     * la adición de la nueva entidad a fallar debido a excepción EntityAlreadyExists
     * Precondiciones: 
     * <br> * {@code entityToAdd} no es nulo y tiene datos válidos.
     */
    public void createEntityWithoutExistenceCheck(EntityAttributes entityToAdd) 
            throws InvalidParametersException {
        
        Assumption.assertNotNull(
                Const.StatusCodes.DBLEVEL_NULL_INPUT, entityToAdd);
        
        entityToAdd.sanitizeForSaving();
        
        if (!entityToAdd.isValid()) {
            throw new InvalidParametersException(entityToAdd.getInvalidityInfo());
        }
        
        Object entity = entityToAdd.toEntity();
        getPM().makePersistent(entity);
        getPM().flush();

        // Espere a que la operación persista
        if(Config.PERSISTENCE_CHECK_DURATION > 0){
            int elapsedTime = 0;
            Object entityCheck = getEntity(entityToAdd);
            while ((entityCheck == null)
                    && (elapsedTime < Config.PERSISTENCE_CHECK_DURATION)) {
                ThreadHelper.waitBriefly();
                entityCheck = getEntity(entityToAdd);
                //comprobar antes de incrementar para evitar problema caso límite
                if (entityCheck == null) {
                    elapsedTime += ThreadHelper.WAIT_DURATION;
                }
            }
            if (elapsedTime == Config.PERSISTENCE_CHECK_DURATION) {
                log.severe("Operation did not persist in time: create"
                        + entityToAdd.getEntityTypeAsString() + "->"
                        + entityToAdd.getIdentificationString());
            }
        }
    }
    
    // TODO: usar este método para subclasses.
    /**
     * Note: Esto no es una eliminación en cascada.<br>
     *   <br> Falla en silencio si no hay tal objeto.
     * <br> Precondiciones: 
     * <br> * {@code courseId} no es null.
     */
    public void deleteEntity(EntityAttributes entityToDelete) {
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, entityToDelete);

        Object entity = getEntity(entityToDelete);

        if (entity == null) {
            return;
        }

        getPM().deletePersistent(entity);
        getPM().flush();
        
        // esperar a que la operación persista
        if(Config.PERSISTENCE_CHECK_DURATION > 0){
            int elapsedTime = 0;
            Object entityCheck = getEntity(entityToDelete);
            while ((entityCheck != null)
                    && (elapsedTime < Config.PERSISTENCE_CHECK_DURATION)) {
                ThreadHelper.waitBriefly();
                entityCheck = getEntity(entityToDelete);
                //comprobar antes de incrementar para evitar problema caso límite
                if (entityCheck == null) {
                    elapsedTime += ThreadHelper.WAIT_DURATION;
                }
            }
            if (elapsedTime == Config.PERSISTENCE_CHECK_DURATION) {
                log.severe("Operation did not persist in time: delete"
                        + entityToDelete.getEntityTypeAsString() + "->"
                        + entityToDelete.getIdentificationString());
            }
        }
    }
    
    public void deleteEntities(Collection<? extends EntityAttributes> entitiesToDelete) {
        
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, entitiesToDelete);
        List<Object> entities = new ArrayList<Object>();
        for(EntityAttributes entityToDelete : entitiesToDelete){
            Object entity = getEntity(entityToDelete);
            if (entity != null) {
                entities.add(entity);
            }
        }
        
        getPM().deletePersistentAll(entities);
        getPM().flush();
    }
    
    public void commitOutstandingChanges() {
        closePM();
    }
    
    protected void closePM() {
        if (!getPM().isClosed()) {
            getPM().close();
        }
    }
    
    public void deletePicture(BlobKey key) throws BlobstoreFailureException {
        BlobstoreServiceFactory.getBlobstoreService().delete(key);
    }
    
    public void deletePictures(BlobKey[] keys) throws BlobstoreFailureException {
        BlobstoreServiceFactory.getBlobstoreService().delete(keys);
    }
    
    /**
     * NOTE: Este método debe ser sobreescrito para todas las subclases tales que le proporcione
     * la Entidad coincidir los EntityAttributes en el parámetro.
     * @return    la Entidad que coincide con la dada {@link EntityAttributes} {@code attributes}
     *             basado en los identificadores de clave predeterminada. Devuelve null si
     *             no existe ya en el almacén de datos. 
     */
    protected abstract Object getEntity(EntityAttributes attributes) ;
    
    protected PersistenceManager getPM() {
        return Datastore.getPersistenceManager();
    }
    
    //las API siguientes son utilizados por los motores de búsqueda Teammates
    protected void putDocument(String indexName, SearchDocument document){
        try{
            SearchManager.putDocument(indexName, document.build());
        } catch (Exception e){
            log.info("Failed to put searchable document in " + indexName + " for " + document.toString());
        }
    }
    
    protected void getDocument(String indexName, String documentId) {
        SearchManager.getDocument(indexName, documentId);
    }
    
    protected Results<ScoredDocument> searchDocuments(String indexName, SearchQuery query) {
        try{
            if(query.getFilterSize() > 0){
                return SearchManager.searchDocuments(indexName, query.toQuery());
            } else {
                return null;
            }
        } catch (SearchQueryException e){
            log.info("Unsupported query for this query string: " + query.toString());
            return null;
        }
    }
    
    protected void deleteDocument(String indexName, String documentId){
        try{
            SearchManager.deleteDocument(indexName, documentId);
        } catch (Exception e){
            log.info("Unable to delete document in the index: " + indexName + " with document id " + documentId);
        }
    }
    
    protected void deleteDocuments(String indexName, String[] documentId){
        SearchManager.deleteDocuments(indexName, documentId);
    }
}
