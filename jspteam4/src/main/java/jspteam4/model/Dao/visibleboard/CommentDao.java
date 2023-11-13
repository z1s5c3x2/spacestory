package jspteam4.model.Dao.visibleboard;

import jspteam4.model.Dto.BoardDto;

public class CommentDao {
	private static CommentDao commentDao = new CommentDao();
    public static CommentDao getInstance() {return commentDao;}
    private CommentDao() {};
    
    
    
    //1. 작성하기
    
    public boolean commentWrite() {
    	return false;
    }
    
    
    //2. 출력하기 (dto 확인)
    
    public BoardDto commentView(){
    	return null;
    }
    
    
    
    //3. 삭제하기
    public boolean commentDelete() {
    	return false;
    }
    
}
