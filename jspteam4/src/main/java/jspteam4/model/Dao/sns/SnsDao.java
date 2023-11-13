package jspteam4.model.Dao.sns;

import java.util.ArrayList;

import jspteam4.model.Dto.sns.SnsDto;



public class SnsDao extends Dao{
	private static SnsDao snsDao = new SnsDao();
    public static SnsDao getInstance() {return snsDao;}
    private SnsDao() {};
	
	
	
	
	
	//1. C 
	
	
	public boolean snsWrite(SnsDto sdt)
	{
		try {
			String sql = "insert into sns(simg,scontent,spwd) VALUES(?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, sdt.getSimg());
			ps.setString(2, sdt.getScontent());
			ps.setString(3, sdt.getSpwd());
			
			return 1 == ps.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("쓰기 에러"+e);
		}
		return false;
	}
	
	
	
	//2. R
    public ArrayList<SnsDto> getList(){
    	ArrayList<SnsDto> list = new ArrayList<>();
    	try {String sql = "select * from sns order by sdate desc";
    		ps=conn.prepareStatement(sql);
    		rs=ps.executeQuery();
    		while(rs.next()) {
    			SnsDto dto = new SnsDto( rs.getInt("sno"),rs.getString("simg"), rs.getString("scontent"),rs.getString("sdate"), rs.getInt("good"), rs.getInt("hate"));
    			list.add(dto);

    		}
    			
			
		} catch (Exception e) {
		System.out.println(e);
		}
    	
    	return list;
    }
	
	
	
	
	
	
	//3. U 
     // 1. 개별 글 출력
    public SnsDto getBoard( int sno ) {
		try {
			String sql = "select * from sns where sno = ?";
					
			ps = conn.prepareStatement(sql);
			ps.setInt( 1 , sno); 
			rs = ps.executeQuery();
			if( rs.next()  ) { // 한개 레코드 조회 if
				
				SnsDto snsDto = new SnsDto(
						rs.getInt("sno"), rs.getString("simg"), 
						rs.getString("scontent")
						);
				
				return snsDto;
			}
		}catch (Exception e) { System.out.println(e); }
		return null;
	}
    // 2. 게시물 수정 
 	public boolean onUpdate(SnsDto dto) {
 		try {
 			String sql = 
 					"update sns set simg = ? , scontent = ? where sno = ? and spwd = ?";
 			ps = conn.prepareStatement(sql);
 			ps.setString(1, dto.getSimg());
 			ps.setString(2, dto.getScontent());
 			ps.setInt(3, dto.getSno());
 			ps.setString(4,dto.getSpwd());
 	
 			int count = ps.executeUpdate(); 
 			if( count == 1 )return true; 
 		}catch (Exception e) {System.out.println(e);	}
 		return false;
 	}
	
	
	
	//4. D
    public boolean onDelete(int sno, String spwd ) {
    	String sql = "delete from sns where sno = ? and spwd = ?";
    	try {
    		ps = conn.prepareStatement(sql);
    		ps.setInt(1, sno);
    		ps.setString(2, spwd);
    		int row = ps.executeUpdate();
    		if(row == 1) return true;
    	}catch(Exception e) {
    		System.out.println("onDelete DB 오류 : " + e);
    	}
    	return false;
    }
    
    //5. D 파일 꺼내기
    public String fileDelete(int sno) {
    	try {String sql = "select * from sns where sno = ?";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1,sno);
    		rs = ps.executeQuery();
    		if(rs.next()) {return rs.getString("simg");}
			
		} catch (Exception e) {
			System.out.println(e);
		}return null;
    	
    	
    }
    
    
    // 좋아요/싫어요 증가
    public boolean updateGoodHate(int sno, String type) {
    	
    	String sql = "update sns set " + type + "= " + type + "+1 where sno = ?";
    	try {
    		ps = conn.prepareStatement(sql);
    		ps.setInt(1, sno);
    		int row = ps.executeUpdate();
    		if(row == 1) return true;
    	}catch(Exception e) {
    		System.out.println("updateGoodHate DB 오류 : " + e);
    	}
    	
    	return false;
    }
}
