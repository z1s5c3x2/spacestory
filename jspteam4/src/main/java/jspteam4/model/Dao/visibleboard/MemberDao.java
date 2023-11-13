package jspteam4.model.Dao.visibleboard;

import java.sql.SQLException;
import java.time.LocalDateTime;

import jspteam4.model.Dao.sns.Dao;
import jspteam4.model.Dto.MemberDto;


public class MemberDao extends Dao{
	private static MemberDao memberDao = new MemberDao();
    public static MemberDao getInstance() {return memberDao;}
    private MemberDao() {};
    
 // 1. 회원가입
 	public boolean signup( MemberDto dto ) {
 		try {
 			String sql = "insert into member( mid , mpwd , memail , mnickname ) values( ? , ? , ? , ? )";
 			ps = conn.prepareStatement(sql);
 			ps.setString( 1 , dto.getMid() );
 			ps.setString( 2 , dto.getMpwd() );
 			ps.setString( 3 , dto.getMemail() );
 			ps.setString( 4 , dto.getMnickname() );
 			int row = ps.executeUpdate();
 			if( row == 1 )return true;
 		}catch (Exception e) {System.out.println(e);}
 		return false;
 	}
 	// 2. 로그인 
 	public boolean login( String mid , String mpwd ) {
 		try {
 			String sql ="select * from member where mid =  ? and mpwd = ? ";
 			ps = conn.prepareStatement(sql);
 			ps.setString( 1 , mid ); ps.setString( 2 , mpwd );
 			rs = ps.executeQuery();
 			if( rs.next() ) return true;
 		}catch (Exception e) { System.out.println(e);}
 		return false;
 	}
 	// 3. 아이디찾기
 	public String findId(String memail) {
 		String foundId = null;
        try {
            String sql = "SELECT mid FROM member WHERE memail = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, memail);
            rs = ps.executeQuery();

            if (rs.next()) {
                foundId = rs.getString("mid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	System.out.println("Dao foundId : " + foundId);
        }
        return foundId;
    }
    
    
  
    
 	// 4. 비밀번호찾기 
 	 public String findPw(String mid, String memail) {
         String foundPwd = null;
         try {
             String sql = "SELECT mpwd FROM member WHERE mid = ? AND memail = ?";
             ps = conn.prepareStatement(sql);
             ps.setString(1, mid);
             ps.setString(2, memail);
             rs = ps.executeQuery();

             if (rs.next()) {
                 foundPwd = rs.getString("mpwd");
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             // 연결, pstmt, rs 닫기
         }
         return foundPwd;
     }
 	// 5. 내정보 호출 
 	public MemberDto info( String mid ) {
 		try {
 			String sql ="select mno , mid , memail , mnickname from member where mid = ?";
 			ps = conn.prepareStatement(sql);
 			ps.setString( 1 , mid );
 			rs = ps.executeQuery();
 			if( rs.next() ) {
 				MemberDto memberDto = MemberDto.builder().mno(rs.getInt("mno")).mid(rs.getString("mid")).memail(rs.getString("memail")).mnickname(rs.getString("mnickname")).build();
 				return memberDto;
 			}
 		}catch (Exception e) { System.out.println(e ); }
 		return null;
 	}
 // 5. 검색 정보 호출 
  	public MemberDto searchinfo( String mnickname ) {
  		try {
  			String sql ="select mno , mnickname from member where mnickname = ?";
  			ps = conn.prepareStatement(sql);
  			ps.setString( 1 , mnickname );
  			rs = ps.executeQuery();
  			
  			if( rs.next() ) {
  				System.out.println(rs.getInt("mno")); System.out.println(rs.getString("mnickname"));
  				MemberDto memberDto = MemberDto.builder().mno(rs.getInt("mno")).mnickname(rs.getString("mnickname")).build();
  				
  				return  memberDto;
  			}
  		}catch (Exception e) { System.out.println(e ); }
  		return null;
  	}
 	
 	// 6. 아이디/이메일 중복검사 [ 인수 : 검사할아이디 / 반환 : true(중복있어) , false(중복없어) 
 		// - type(mid,memail) : 필드명 / data(입력받은mid,memail) : 필드에서 찾을 값 
 	public boolean findIdOrEmail( String type , String data ) {
 		try {
 			String sql = "select * from member where "+type+" = ?";
 			ps = conn.prepareStatement(sql);
 			ps.setString( 1 , data );
 			rs = ps.executeQuery();
 			// [ while : 결과 레코드 여러개 검사 vs if : 결과 레코드 한개 검사 ]
 			if( rs.next() ) return true;
 		}catch (Exception e) {System.out.println(e);}
 		return false;
 	}
 	// 7. 회원수정
 	public boolean mupdate( int mno , String mnickname , String mpwd) {
 		try {
 			String sql = "update member set mnickname = ? , mpwd = ? where mno = ?";
 			ps = conn.prepareStatement(sql);
 			ps.setString( 1 , mnickname  ); ps.setString( 2 , mpwd  ); ps.setInt( 3 , mno  );
 			int count = ps.executeUpdate();
 			if ( count == 1 ) return true;
 		} catch (Exception e) { System.out.println(e);}
 		return false;
 	}
 	// 8. 회원탈퇴 [ 삭제할회원번호 , 검증할패스워드 ]
 	public boolean mdelete( int mno , String mpwd ) {
 		try {
 			String sql ="delete from member where mno = ? and mpwd = ?";
 			ps = conn.prepareStatement(sql);
 			ps.setInt( 1 , mno); ps.setString(2, mpwd);
 			int count = ps.executeUpdate();
 			if( count == 1 ) return true; // 삭제성공 => 회원탈퇴
 		}catch (Exception e) {System.out.println(e);}
 		return false; // 회원번호 또는 입력받은 패스워드 일치하지 않거나
 	}
 	
 	// 9. 리뷰 작성자에 사용될 mno를 이용해 mnickname 찾기
 	public String mnoTomnickname(int mno) {
 		
 		try {
 			String sql = "select mnickname from member where mno = " + mno;
 			ps = conn.prepareStatement(sql);
 			rs = ps.executeQuery();
 			if(rs.next()) return rs.getString(1);
 		}catch(Exception e) {
 			System.out.println("mnoTomnickname DB 오류 : " + e);
 		}
 		
 		return null;
 	}
 	// 10. 회원 수정용 mno로 기존닉네임 , 비밀번호 가져오기
 	public MemberDto mnofindnicknamepw( int mno ) {
  		try {
  			String sql ="select mnickname , mpwd from member where mno = ?";
  			ps = conn.prepareStatement(sql);
  			ps.setInt( 1 , mno );
  			rs = ps.executeQuery();
  			
  			if( rs.next() ) {
  				System.out.println(rs.getString("mpwd")); System.out.println(rs.getString("mnickname"));
  				MemberDto memberDto = MemberDto.builder().mnickname(rs.getString("mnickname")).mpwd(rs.getString("mpwd")).build();
  				
  				return  memberDto;
  			}
  		}catch (Exception e) { System.out.println(e ); }
  		return null;
  	}
}
