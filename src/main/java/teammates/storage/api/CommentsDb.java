package teammates.storage.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.Query;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

import teammates.common.datatransfer.CommentAttributes;
import teammates.common.datatransfer.CommentRecipientType;
import teammates.common.datatransfer.CommentSearchResultBundle;
import teammates.common.datatransfer.CommentSendingState;
import teammates.common.datatransfer.CommentStatus;
import teammates.common.datatransfer.EntityAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.Utils;
import teammates.storage.entity.Comment;
import teammates.storage.search.CommentSearchDocument;
import teammates.storage.search.CommentSearchQuery;

/**
 * Maneja operaciones CRUD para {@link Comment}.
 * La API utiliza clases de transferencia de datos (es decir, * Atributos) en lugar de las clases con persistencia.
 */
public class CommentsDb extends EntitiesDb{
    
    public static final String ERROR_UPDATE_NON_EXISTENT = "Trying to update non-existent Comment: ";
    private static final Logger log = Utils.getLogger();
    
    /**
     * Este método es solo para probar
     * @param commentsToAdd
     * @throws InvalidParametersException
     */
    public void createComments(Collection<CommentAttributes> commentsToAdd) throws InvalidParametersException{
        List<EntityAttributes> commentsToUpdate = createEntities(commentsToAdd);
        for(EntityAttributes entity : commentsToUpdate){
            CommentAttributes comment = (CommentAttributes) entity;
            try {
                updateComment(comment);
            } catch (EntityDoesNotExistException e) {
                // Esta situación no se ha probado como la replicación de tal situación 
                // es difícil durante la prueba
                Assumption.fail("Entity found be already existing and not existing simultaneously");
            }
        }
    }
    
    /**
     * Precondiciones: 
     * <br> * {@code entityToAdd} no es nulo y tiene datos válidos.
     */
    @Override
    public CommentAttributes createEntity(EntityAttributes entityToAdd) 
            throws InvalidParametersException, EntityAlreadyExistsException{
        Comment createdEntity = (Comment) super.createEntity(entityToAdd);
        if(createdEntity == null){
            log.info("Trying to get non-existent Comment, possibly entity not persistent yet.");
            return null;
        } else{
            CommentAttributes createdComment = new CommentAttributes(createdEntity);
            return createdComment;
        }
    }
    
    /**
     * Eliminar Documento de la búsqueda por el comentario dado
     * @param commentToDelete
     */
    public void deleteDocument(CommentAttributes commentToDelete){
        if(commentToDelete.getCommentId() == null){
            CommentAttributes comment = getComment(commentToDelete);
            deleteDocument(Const.SearchIndex.COMMENT, comment.getCommentId().toString());
        } else {
            deleteDocument(Const.SearchIndex.COMMENT, commentToDelete.getCommentId().toString());
        }
    }
    
    /*
     * Conseguir comentario para el id del comentario
     */
    public CommentAttributes getComment(Long commentId){
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, commentId);
        
        Comment comment = getCommentEntity(commentId);
        if(comment == null){
            log.info("Trying to get non-existent Comment: " + commentId);
            return null;
        } else{
            return new CommentAttributes(comment);
        }
    }
    
    /*
     * Conseguir comentario para un atributo camentario dado
     */
    public CommentAttributes getComment(CommentAttributes commentToGet){
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, commentToGet);
        Comment comment = null;
        if(commentToGet.getCommentId() != null){
            comment = getCommentEntity(commentToGet.getCommentId());
        }
        if(comment == null){
            comment = getCommentEntity(commentToGet.courseId, commentToGet.giverEmail, commentToGet.recipientType,
                commentToGet.recipients, commentToGet.createdAt);
        }
        if(comment == null){
            log.info("Trying to get non-existent Comment: " + commentToGet);
            return null;
        } else{
            return new CommentAttributes(comment);
        }
    }
    
    /*
     * Conseguir comentario para un email dado
     */
    public List<CommentAttributes> getCommentsForGiver(String courseId, String giverEmail){
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, giverEmail);
        
        List<Comment> comments = getCommentEntitiesForGiver(courseId, giverEmail);
        List<CommentAttributes> commentAttributesList = new ArrayList<CommentAttributes>();
        
        for(Comment comment: comments){
            commentAttributesList.add(new CommentAttributes(comment));
        }
        return commentAttributesList;
    }
    
    /*
     * Conseguir comentario para un email dado y elestado del comentario
     */
    public List<CommentAttributes> getCommentsForGiverAndStatus(String courseId, String giverEmail, CommentStatus status){
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, giverEmail);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, status);
        
        List<Comment> comments = getCommentEntitiesForGiverAndStatus(courseId, giverEmail, status);
        List<CommentAttributes> commentAttributesList = new ArrayList<CommentAttributes>();
        
        for(Comment comment: comments){
            commentAttributesList.add(new CommentAttributes(comment));
        }
        return commentAttributesList;
    }
    
    /*
     * Obtener comentarios con proyecto de estado
     */
    public List<CommentAttributes> getCommentDrafts(String giverEmail){
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, giverEmail);
        
        List<Comment> comments = getCommentEntitiesForDraft(giverEmail);
        List<CommentAttributes> commentAttributesList = new ArrayList<CommentAttributes>();
        
        for(Comment comment: comments){
            commentAttributesList.add(new CommentAttributes(comment));
        }
        return commentAttributesList;
    }

    /*
     * Consigue comentario para el correo electrónico del receptor
     */
    public List<CommentAttributes> getCommentsForReceiver(String courseId, CommentRecipientType recipientType, String receiverEmail){
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, recipientType);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, receiverEmail);
        
        List<Comment> comments = getCommentEntitiesForRecipients(courseId, recipientType, receiverEmail);
        List<CommentAttributes> commentAttributesList = new ArrayList<CommentAttributes>();
        
        for(Comment comment: comments){
            commentAttributesList.add(new CommentAttributes(comment));
        }
        return commentAttributesList;
    }
    
    /*
     * Consigue comentar para el espectador (que puede ver el comentario) tipo
     */
    public List<CommentAttributes> getCommentsForCommentViewer(String courseId, CommentRecipientType commentViewerType){
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, commentViewerType);
        
        List<Comment> comments = getCommentEntitiesForCommentViewer(courseId, commentViewerType);
        List<CommentAttributes> commentAttributesList = new ArrayList<CommentAttributes>();
        
        for(Comment comment: comments){
            commentAttributesList.add(new CommentAttributes(comment));
        }
        return commentAttributesList;
    }
    
    /*
     * Consigue comentario para el Estado que envía (ENVIADO | ENVÍO | PENDIENTE)
     */
    public List<CommentAttributes> getCommentsForSendingState(String courseId, CommentSendingState state){
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        
        List<Comment> comments = getCommentEntitiesForSendingState(courseId, state);
        List<CommentAttributes> commentAttributesList = new ArrayList<CommentAttributes>();
        
        for(Comment comment: comments){
            commentAttributesList.add(new CommentAttributes(comment));
        }
        return commentAttributesList;
    }
    
    /*
     * Actualización de comentarios de un estado antiguo a un nuevo estado
     */
    public void updateComments(String courseId, CommentSendingState oldState, CommentSendingState newState){
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        
        List<Comment> comments = getCommentEntitiesForSendingState(courseId, oldState);
        
        for(Comment comment: comments){
            comment.setSendingState(newState);
        }
        
        getPM().close();
    }

    /**
     * Precondiciones: 
     * <br> * {@code newAttributes} no es nulo y tiene datos válidos.
     */
    public CommentAttributes updateComment(CommentAttributes newAttributes) throws InvalidParametersException, EntityDoesNotExistException{
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT,  newAttributes);
        
        newAttributes.sanitizeForSaving();
        
        if (!newAttributes.isValid()) {
            throw new InvalidParametersException(newAttributes.getInvalidityInfo());
        }
        Comment comment = (Comment) getEntity(newAttributes);
        
        if (comment == null) {
            throw new EntityDoesNotExistException(ERROR_UPDATE_NON_EXISTENT + newAttributes.toString());
        }

        if(newAttributes.commentText != null){
            comment.setCommentText(newAttributes.commentText);
        }
        if(newAttributes.showCommentTo != null){
            comment.setShowCommentTo(newAttributes.showCommentTo);
        }
        if(newAttributes.showGiverNameTo != null){
            comment.setShowGiverNameTo(newAttributes.showGiverNameTo);
        }
        if(newAttributes.showRecipientNameTo != null){
            comment.setShowRecipientNameTo(newAttributes.showRecipientNameTo);
        }
        if(newAttributes.status != null){
            comment.setStatus(newAttributes.status);
        }
        if(newAttributes.recipientType != null){
            comment.setRecipientType(newAttributes.recipientType);
        }
        if(newAttributes.recipients != null){
            comment.setRecipients(newAttributes.recipients);
        }
        comment.setSendingState(newAttributes.sendingState);
        
        getPM().close();
        
        CommentAttributes updatedComment = new CommentAttributes(comment);
        
        return updatedComment;
    }
    
    /*
     * Actualizar el email antiguo del instructor utilizado en el comentario con uno nuevo
     */
    public void updateInstructorEmail(String courseId, String oldInstrEmail, String updatedInstrEmail) {
        
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, oldInstrEmail);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, updatedInstrEmail);
        
        updateInstructorEmailAsGiver(courseId, oldInstrEmail, updatedInstrEmail);
        // por ahora, los instructores sólo pueden ser dadores
        // updateInstructorEmailAsRecipient(courseId, oldInstrEmail, updatedInstrEmail);
    }
    
    private void updateInstructorEmailAsGiver(String courseId, String oldInstrEmail, String updatedInstrEmail) {
        List<Comment> giverComments = this.getCommentEntitiesForGiver(courseId, oldInstrEmail);
        
        for (Comment giverComment : giverComments) {
            giverComment.setGiverEmail(updatedInstrEmail);
        }
        
        getPM().close();
    }
    
    // Por ahora, este método no está siendo utilizado como instructor no puede ser receptor
    @SuppressWarnings("unused")
    private void updateInstructorEmailAsRecipient(String courseId, String oldInstrEmail, String updatedInstrEmail) {
        List<Comment> recipientComments = this.getCommentEntitiesForRecipients(courseId, 
                CommentRecipientType.INSTRUCTOR, oldInstrEmail);
        
        for (Comment recipientComment : recipientComments) {
            recipientComment.setGiverEmail(updatedInstrEmail);
        }
        
        getPM().close();
    }
    
    /*
     * Actualizar email del estudiante utilizado en el comentario con uno nuevo
     */
    public void updateStudentEmail(String courseId, String oldStudentEmail, String updatedStudentEmail) {

        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, oldStudentEmail);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, updatedStudentEmail);
        
        updateStudentEmailAsRecipient(courseId, oldStudentEmail, updatedStudentEmail);
    }

    private void updateStudentEmailAsRecipient(String courseId, String oldStudentEmail, String updatedStudentEmail) {
        List<Comment> recipientComments = this.getCommentEntitiesForRecipients(courseId, 
                CommentRecipientType.PERSON, oldStudentEmail);
        
        for (Comment recipientComment : recipientComments) {
            recipientComment.getRecipients().remove(oldStudentEmail);
            recipientComment.getRecipients().add(updatedStudentEmail);
        }
        
        getPM().close();
    }
    
    /*
     * Eliminar los comentarios dados por cierto instructor
     */
    public void deleteCommentsByInstructorEmail(String courseId, String email) {
        
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, email);
        
        List<Comment> giverComments = this.getCommentEntitiesForGiver(courseId, email);
        // for now, this list is empty
//        List<Comment> recipientComments = this.getCommentEntitiesForRecipients(courseId, 
//                CommentRecipientType.INSTRUCTOR, email);
//        getPM().deletePersistentAll(recipientComments);
        
        getPM().deletePersistentAll(giverComments);
        
        getPM().flush();
    }
    
    /*
     * Eliminar los comentarios dados a cierto estudiante
     */
    public void deleteCommentsByStudentEmail(String courseId, String email) {

        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, email);
        
        // student right now cannot be giver, so no need to&should not check for giver
        List<Comment> recipientComments = this.getCommentEntitiesForRecipients(courseId, 
                CommentRecipientType.PERSON, email);
        
        getPM().deletePersistentAll(recipientComments);
        
        getPM().flush();
    }
    
    /*
     * Eliminar los comentarios dados a determinado equipo
     */
    public void deleteCommentsForTeam(String courseId, String teamName) {
        
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, teamName);
        
        // student right now cannot be giver, so no need to&should not check for giver
        List<Comment> recipientComments = this.getCommentEntitiesForRecipients(courseId, 
                CommentRecipientType.TEAM, teamName);
        
        getPM().deletePersistentAll(recipientComments);
        getPM().flush();
    }
    
    /*
     * Eliminar los comentarios dados a cierta sección
     */
    public void deleteCommentsForSection(String courseId, String sectionName) {
        
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, sectionName);
        
        // student right now cannot be giver, so no need to&should not check for giver
        List<Comment> recipientComments = this.getCommentEntitiesForRecipients(courseId, 
                CommentRecipientType.SECTION, sectionName);
        
        getPM().deletePersistentAll(recipientComments);
        getPM().flush();
    }
    
    /*
     * Eliminar comentarios en determinado curso
     */
    public void deleteCommentsForCourse(String courseId) {

        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseId);
        
        List<Comment> courseComments = getCommentEntitiesForCourse(courseId);
        
        getPM().deletePersistentAll(courseComments);
        getPM().flush();
    }
    
    /*
     * Eliminar comentarios en determinado cursos
     */
    public void deleteCommentsForCourses(List<String> courseIds) {
        
        Assumption.assertNotNull(Const.StatusCodes.DBLEVEL_NULL_INPUT, courseIds);
        
        List<Comment> commentsToDelete = getCommentEntitiesForCourses(courseIds);
        
        getPM().deletePersistentAll(commentsToDelete);
        getPM().flush();
    }
    
    /*
     * Crear o actualizar el documento de búsqueda para el comentario dado
     */
    public void putDocument(CommentAttributes comment){
        putDocument(Const.SearchIndex.COMMENT, new CommentSearchDocument(comment));
    }
    
    /**
     * Buscar comentarios
     * @return {@link CommentSearchResultBundle}
     */
    public CommentSearchResultBundle search(String queryString, String googleId, String cursorString){
        if(queryString.trim().isEmpty())
            return new CommentSearchResultBundle();
        
        Results<ScoredDocument> results = searchDocuments(Const.SearchIndex.COMMENT, 
                new CommentSearchQuery(googleId, queryString, cursorString));
        
        return new CommentSearchResultBundle().fromResults(results, googleId);
    }
    
    /**
     * @deprecated No escalable. No utilice a menos que en las funciones de administración.
     */
    @Deprecated
    public List<CommentAttributes> getAllComments() {
        
        List<CommentAttributes> list = new ArrayList<CommentAttributes>();
        List<Comment> entities = getAllCommentEntities();
        for(Comment comment: entities){
            list.add(new CommentAttributes(comment));
        }
        return list;
    }
    
    private List<Comment> getAllCommentEntities() {
        
        String query = "select from " + Comment.class.getName();
            
        @SuppressWarnings("unchecked")
        List<Comment> commentList = (List<Comment>) getPM()
                .newQuery(query).execute();
    
        return getCommentsWithoutDeletedEntity(commentList);
    }

    private List<Comment> getCommentsWithoutDeletedEntity(
            List<Comment> commentList) {
        List<Comment> resultList = new ArrayList<Comment>();
        for(Comment c:commentList){
            if(!JDOHelper.isDeleted(c)){
                resultList.add(c);
            }
        }
        
        return resultList;
    }
    
    private List<Comment> getCommentEntitiesForCourse(String courseId) {
        Query q = getPM().newQuery(Comment.class);
        q.declareParameters("String courseIdParam");
        q.setFilter("courseId == courseIdParam");
        
        @SuppressWarnings("unchecked")
        List<Comment> commentsForCourse = (List<Comment>) q.execute(courseId);
        
        return commentsForCourse;
    }
    
    private List<Comment> getCommentEntitiesForCourses(List<String> courseIds) {
        Query q = getPM().newQuery(Comment.class);
        q.setFilter(":p.contains(courseId)");
        
        @SuppressWarnings("unchecked")
        List<Comment> commentsForCourse = (List<Comment>) q.execute(courseIds);
        
        return commentsForCourse;
    }
    
    private List<Comment> getCommentEntitiesForSendingState(String courseId, CommentSendingState sendingState){
        Query q = getPM().newQuery(Comment.class);
        q.declareParameters("String courseIdParam, String sendingStateParam");
        q.setFilter("courseId == courseIdParam && sendingState == sendingStateParam");
        
        @SuppressWarnings("unchecked")
        List<Comment> commentList = (List<Comment>) q.execute(courseId, sendingState.toString());
        
        return getCommentsWithoutDeletedEntity(commentList);
    }
    
    private List<Comment> getCommentEntitiesForGiver(String courseId, String giverEmail){
        Query q = getPM().newQuery(Comment.class);
        q.declareParameters("String courseIdParam, String giverEmailParam");
        q.setFilter("courseId == courseIdParam && giverEmail == giverEmailParam");
        
        @SuppressWarnings("unchecked")
        List<Comment> commentList = (List<Comment>) q.execute(courseId, giverEmail);
        
        return getCommentsWithoutDeletedEntity(commentList);
    }
    
    private List<Comment> getCommentEntitiesForGiverAndStatus(String courseId,
            String giverEmail, CommentStatus status) {
        Query q = getPM().newQuery(Comment.class);
        q.declareParameters("String courseIdParam, String giverEmailParam, String statusParam");
        q.setFilter("courseId == courseIdParam && giverEmail == giverEmailParam && status == statusParam");
        
        @SuppressWarnings("unchecked")
        List<Comment> commentList = (List<Comment>) q.execute(courseId, giverEmail, status.toString());
        
        return getCommentsWithoutDeletedEntity(commentList);
    }
    
    private List<Comment> getCommentEntitiesForDraft(String giverEmail) {
        Query q = getPM().newQuery(Comment.class);
        q.declareParameters("String giverEmailParam, String statusParam");
        q.setFilter("giverEmail == giverEmailParam && status == statusParam");
        
        @SuppressWarnings("unchecked")
        List<Comment> commentList = (List<Comment>) q.execute(giverEmail, CommentStatus.DRAFT.toString());
        
        return getCommentsWithoutDeletedEntity(commentList);
    }
    
    private List<Comment> getCommentEntitiesForRecipients(String courseId, CommentRecipientType recipientType, String recipient){
        Query q = getPM().newQuery(Comment.class);
        q.declareParameters("String courseIdParam, String recipientTypeParam, String receiverParam");
        q.setFilter("courseId == courseIdParam && recipientType == recipientTypeParam && recipients.contains(receiverParam)");
        
        @SuppressWarnings("unchecked")
        List<Comment> commentList = (List<Comment>) q.execute(courseId, recipientType.toString(), recipient);
        
        return getCommentsWithoutDeletedEntity(commentList);
    }
    
    private List<Comment> getCommentEntitiesForCommentViewer(String courseId, CommentRecipientType commentViewerType){
        Query q = getPM().newQuery(Comment.class);
        q.declareParameters("String courseIdParam, String commentViewerTypeParam");
        q.setFilter("courseId == courseIdParam "
                + "&& showCommentTo.contains(commentViewerTypeParam)");
        @SuppressWarnings("unchecked")
        List<Comment> commentList = (List<Comment>) q.execute(courseId, commentViewerType.toString());
        
        return getCommentsWithoutDeletedEntity(commentList);
    }

    @Override
    protected Object getEntity(EntityAttributes attributes) {
        CommentAttributes commentToGet = (CommentAttributes) attributes;
        if(commentToGet.getCommentId() != null){
            return getCommentEntity(commentToGet.getCommentId());
        } else{
            return getCommentEntity(commentToGet.courseId, commentToGet.giverEmail, commentToGet.recipientType,
                    commentToGet.recipients, commentToGet.createdAt);
        }
    }
    
    // Obtiene una entidad comentario si la ID es conocida
    private Comment getCommentEntity(Long commentId) {
        Query q = getPM().newQuery(Comment.class);
        q.declareParameters("Long commentIdParam");
        q.setFilter("commentId == commentIdParam");
        
        @SuppressWarnings("unchecked")
        List<Comment> commentList = (List<Comment>) q.execute(commentId);

        if (commentList.isEmpty() || JDOHelper.isDeleted(commentList.get(0))) {
            return null;
        }
        return commentList.get(0);
    }
    
    private Comment getCommentEntity(String courseId, String giverEmail, CommentRecipientType recipientType,
            Set<String> recipients, Date date) {
        String firstRecipient = recipients.iterator().next();
        List<Comment> commentList = getCommentEntitiesForRecipients(courseId, recipientType, firstRecipient);
        
        if(commentList.isEmpty()){
            return null;
        }
        
        //JDO query can't seem to handle Text comparison correctly,
        //we have to compare the texts separately.
        for(Comment comment : commentList){
            if(!JDOHelper.isDeleted(comment) 
                    && comment.getGiverEmail().equals(giverEmail)
                    && comment.getCreatedAt().equals(date)
                    && comment.getRecipients().equals(recipients)) {
                return comment;
            }
        }
        
        return null;
    }
}
