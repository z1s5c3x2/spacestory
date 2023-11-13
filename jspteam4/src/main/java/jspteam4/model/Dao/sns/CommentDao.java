package jspteam4.model.Dao.sns;

import java.util.ArrayList;

import jspteam4.model.Dto.sns.CommentDto;
import jspteam4.model.Dto.sns.SnsDto;



public class CommentDao extends Dao{
	private static CommentDao commentDao = new CommentDao();
    public static CommentDao getInstance () {return commentDao;}
    private CommentDao() {};
    
    
    //1.등록 
    public boolean commnetWrite(int sno,String cpwd,String ccontent) {
    	
    	String sql = "insert into comment(sno, cpwd, ccontent) values(?,?,?)";
    	try {
    		ps = conn.prepareStatement(sql);
    		ps.setInt(1, sno);
    		ps.setString(2, cpwd);
    		ps.setString(3, ccontent);
    		int row = ps.executeUpdate();
    		if(row == 1) return true;
    	}catch(Exception e) {
    		System.out.println("commentWrite DB 오류 : " + e);
    	}
    	return false;
    }
    
    
    //2.삭제
    public boolean commentDelete(int cno,String cpwd)
    {
    	try {
			String sql = "delete from comment where cno = ? and cpwd = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, cno);
			ps.setString(2, cpwd);
			return 1 == ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("답글 삭제 에러"+e);
		}
    	
    	return false;
    }
    
    
    
    //3.출력
    public ArrayList<CommentDto> getList( int sno ){
    	ArrayList<CommentDto> list = new ArrayList<>();
    	try {String sql = "select * from comment where sno = ? order by cdate desc";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, sno);
    		rs=ps.executeQuery();
    		while(rs.next()) {
    			CommentDto dto = new CommentDto( rs.getInt("cno"),rs.getString("ccontent"),rs.getString("cpwd"),rs.getString("cdate"),rs.getInt("sno"));
    			list.add(dto);

    		}
    			
			
		} catch (Exception e) {
		System.out.println(e);
		}
    	return list;
    }
	
    
    
    
    //4.검색
    public ArrayList<SnsDto> search(String search){
    	 ArrayList<SnsDto> list =new ArrayList<>();
    	 try {
    		 String sql = "select * from sns where scontent LIKE '%"+search+"%';";
    		 ps=conn.prepareStatement(sql); 
    		 rs=ps.executeQuery();
    		 while(rs.next()) {
    			 SnsDto snsDto = new SnsDto(rs.getInt("sno"),rs.getString("simg"), rs.getString("scontent"),rs.getString("sdate"),rs.getInt("good"),rs.getInt("hate"));
    			 list.add(snsDto);
    		 }
			
		} catch (Exception e) {
			System.out.println(e);
		}
    	
    	 
    	return list;
    	
    }
    //5. 검색된 게시물의 개수
    public int viewSearch(String search) {
    	try {
    		String sql = "select count(*) from sns s where scontent LIKE '%"+search+"%';";
	    	ps=conn.prepareStatement(sql);
	    	rs=ps.executeQuery();
	    	if(rs.next()) {
	    	
	    		return rs.getInt(1);
	    		
	    	}
				
		} catch (Exception e) {
			System.out.println(e);
		}
    	return 0;
    }
    
}
