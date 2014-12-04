package teammates.storage.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.Query;

import teammates.common.datatransfer.CourseAttributes;
import teammates.common.datatransfer.EntityAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.Utils;
import teammates.storage.entity.Comment;
import teammates.storage.entity.Course;

/**
 * Maneja operaciones CRUD para {@link Comment}.
 * La API utiliza clases de transferencia de datos (es decir, * Atributos) en lugar de las clases con persistencia.
 */
public class CoursesDb extends EntitiesDb {
    
    /* Explicación: En base a las políticas para el componente de almacenamiento, esta clase
     * no maneja en cascada. Se trata los valores no válidos como una excepción. 
     * 
     */

    public static final String ERROR_UPDATE_NON_EXISTENT_COURSE = "Trying to update a Course that doesn't exist: ";
    
    @SuppressWarnings("unused")
    private static final Logger log = Utils.getLogger();
    
    public void createCourses(Collection<CourseAttributes> coursesToAdd) throws InvalidParametersException{
        
        List<EntityAttributes> coursesToUpdate = createEntities(coursesToAdd);
        for(EntityAttributes entity : coursesToUpdate){
            CourseAttributes course = (CourseAttributes) entity;
            try {
                updateCourse(course);
            } catch (EntityDoesNotExistException e) {
                // Esta situación no se ha probado como la replicación de tal situación 
                // es difícil durante la prueba
                Assumption.fail("Entity found be already existing and not existing simultaneously");
            }
        }
    }

    /**
     * Precondiciones: <br>
     * * Todos los parametrosno son nulos. 
     * @return Null si no se encuentra.
     */
    public CourseAttributes getCourse(String courseId) {
        
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        
        Course c = getCourseEntity(courseId);

        if (c == null) {
            return null;
        }

        return new CourseAttributes(c);
    }
    
    
    /**
     * @deprecated No escalable. Utilice sólo en las funciones de administración. 
     */
    @Deprecated
    public List<CourseAttributes> getAllCourses() {
        
        Query q = getPM().newQuery(Course.class);
        
        @SuppressWarnings("unchecked")
        List<Course> courseList = (List<Course>) q.execute();
    
        List<CourseAttributes> courseDataList = new ArrayList<CourseAttributes>();
        for (Course c : courseList) {
            courseDataList.add(new CourseAttributes(c));
        }
    
        return courseDataList;
    }

    /**
     * Actualiza el curso.<br>
     * Actualizaciones de estado de sólo curso Archivado.<br>
     * No sigue la política de mantener vigente ' <br> 
     * Precondiciones: <br>
     * * {@code courseToUpdate} es no null. <br>
     * @throws InvalidParametersException, EntityDoesNotExistException
     */
    public void updateCourse(CourseAttributes courseToUpdate) throws InvalidParametersException, EntityDoesNotExistException {
        
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseToUpdate);
        
        courseToUpdate.sanitizeForSaving();
        
        if (!courseToUpdate.isValid()) {
            throw new InvalidParametersException(courseToUpdate.getInvalidityInfo());
        }
        
        Course courseEntityToUpdate = getCourseEntity(courseToUpdate.id);
        
        if (courseEntityToUpdate == null) {
            throw new EntityDoesNotExistException(ERROR_UPDATE_NON_EXISTENT_COURSE);
        }
        
        courseEntityToUpdate.setArchiveStatus(Boolean.valueOf(courseToUpdate.isArchived));
        
        getPM().close();
    }
    

    /**
     * Note: This is a non-cascade delete.<br>
     *   <br> Falla en silencio si no hay tal objeto.
     * <br> Preconditions: 
     * <br> * {@code courseId} es no null.
     */
    public void deleteCourse(String courseId) {
        
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);

        CourseAttributes entityToDelete = new CourseAttributes();
        entityToDelete.id = courseId;
        
        deleteEntity(entityToDelete);
    }
    
    @Override
    protected Object getEntity(EntityAttributes attributes) {
            
        return getCourseEntity( ((CourseAttributes) attributes).id );
    }


    private Course getCourseEntity(String courseId) {
        
        Query q = getPM().newQuery(Course.class);
        q.declareParameters("String courseIdParam");
        q.setFilter("ID == courseIdParam");
        
        @SuppressWarnings("unchecked")
        List<Course> courseList = (List<Course>) q.execute(courseId);
        
        if (courseList.isEmpty() || JDOHelper.isDeleted(courseList.get(0))) {
            return null;
        }
    
        return courseList.get(0);
    }
}
