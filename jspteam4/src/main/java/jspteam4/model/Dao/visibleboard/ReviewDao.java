package jspteam4.model.Dao.visibleboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jspteam4.model.Dao.sns.Dao;
import jspteam4.model.Dto.ReviewDto;

public class ReviewDao extends Dao {
	
	private static ReviewDao reviewDao = new ReviewDao();
    public static ReviewDao getInstance() {return reviewDao;}
    private ReviewDao() {};
    
    // 리뷰 작성
    public boolean reviewWrite(String rcontent, int rsender, int rreceiver, int rscore) {
    	
    	try {
    		String sql = "insert into review(rcontent, rsender, rreceiver, rscore) values(?,?,?,?)";
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, rcontent);
    		ps.setInt(2, rsender);
    		ps.setInt(3, rreceiver);
    		ps.setInt(4, rscore);
    		ps.execute();
    		return true;
    	}catch(Exception e) {
    		System.out.println("reviewWrite DB 오류 : " + e);
    	}
    	
    	return false;
    }
    
    // 개인 리뷰 확인
    public ArrayList<ReviewDto> reviewView(int mno , int listsize , int startrow) {
    	
    	ArrayList<ReviewDto> list = new ArrayList<>();
    	
    	try {
    		String sql = "select * from review where rreceiver = ? order by rdate desc limit ? , ? ";
    		ps = conn.prepareStatement(sql);
    		ps.setInt(1, mno); ps.setInt( 2 , startrow); ps.setInt( 3 , listsize);
    		rs = ps.executeQuery();
    		while(rs.next()) {
    			ReviewDto dto = ReviewDto.builder().rno(rs.getInt("rno")).rcontent(rs.getString("rcontent"))
    					.rdate(rs.getString("rdate")).rsender(rs.getInt("rsender"))
    					.rreceiver(rs.getInt("rreceiver")).rscore(rs.getInt("rscore")).build();
    			
    			list.add(dto);
    		}
    		
    	}catch(Exception e) {
    		System.out.println("reviewView DB 오류 : " + e);
    	}
    	
    	return list;
    }
    // 리뷰 조회시 총 게시물 수 
    public int getTotalSize( int mno ) {
    	System.out.println("getTotalSize 에 들어온 mno : "+ mno);
		try {
			String sql = "select count(*) from review where rreceiver = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1,mno);
			rs = ps.executeQuery();
			if( rs.next() ) return rs.getInt(1);
		}catch (Exception e) { System.out.println("getTotalSize : " + e);}
		return 0;
	}
    // 리뷰 수정
    public boolean reviewUpdate(String rcontent, int rscore, int rno) {
    	try {
    		String sql = "update review set rcontent = ?, rscore = ? where rno = ? ";
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, rcontent);
    		ps.setInt(2, rscore);
    		ps.setInt(3, rno);
    		ps.execute();
    		return true;
    	}catch(Exception e) {
    		System.out.println("reviewUpdate DB 오류 : " + e);
    	}
    	return false;
    }
    
    // 리뷰 삭제
    public boolean reviewDelete(int rno) {
    	try {
    		String sql = "delete from review where rno = ?";
    		ps = conn.prepareStatement(sql);
    		ps.setInt(1, rno);
    		ps.execute();
    		return true;
    	}catch(Exception e) {
    		System.out.println("reviewDelete DB 오류 : " + e);
    	}
    	return false;
    }
}
