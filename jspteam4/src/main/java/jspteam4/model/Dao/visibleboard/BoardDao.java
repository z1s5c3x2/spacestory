package jspteam4.model.Dao.visibleboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jspteam4.model.Dao.sns.Dao;
import jspteam4.model.Dto.BoardDto;
import jspteam4.model.Dto.CategoryDto;
import jspteam4.model.Dto.PageDto;

public class BoardDao extends Dao{
	private static BoardDao boardDao = new BoardDao();
    public static BoardDao getInstance() {return boardDao;}
    private BoardDao() {};
	
    
    //1. 작성하기
    public boolean boardWrite(BoardDto _bdt) {
    	try {
			String sql = "insert into board(btitle,bcontent,blat,blng,mno,cno) values(?,?,?,?,?,?)";
			ps =conn.prepareStatement(sql);
			ps.setString(1, _bdt.getBtitle());
			ps.setString(2, _bdt.getBcontent());
			ps.setString(3, _bdt.getBlat());
			ps.setString(4, _bdt.getBlng());
			ps.setInt(5, _bdt.getMno());
			ps.setInt(6, _bdt.getCno());
			return 1==ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("작성 에러" +e);
		}
    	return false;
    }
    
    
    
    
    
    
    //2. 수정하기
    public boolean boardUpdate(BoardDto _bdt,int mno) {
    	try {
    		
    		String sql = "UPDATE board SET btitle = ?,bcontent = ?,cno = ? WHERE mno = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1,_bdt.getBtitle());
			ps.setString(2,_bdt.getBcontent());
			ps.setInt(3,_bdt.getCno());
			ps.setInt(4, mno);
			return  ps.executeUpdate() == 1;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("수정 에러"+e);
		}
    	
    	return false;
    }
    
    
    
    
    
    //3. 출력하기 (dto 확인하기)
    								// 북 남 서 동
    public ArrayList<BoardDto> boardGetList(String pa,String qa,String ha,String oa,
    		int cno,int start,int end,String sSort,Double clng,Double clat) {
    	ArrayList<BoardDto> res = new ArrayList<>();
    	try {
    		System.out.println(start);
    		System.out.println(end);
			String sql = "SELECT\r\n"
					+ "    b.bno,\r\n"
					+ "    b.btitle,\r\n"
					+ "    b.bcontent,\r\n"
					+ "    b.bdate,\r\n"
					+ "    b.mno,\r\n"
					+ "    b.blat,\r\n"
					+ "    b.blng,\r\n"
					+ "    b.blng,\r\n"
					+ "    b.cno,\r\n"
					+ "    m.mnickname,\r\n"
					+ "    m.mid,\r\n"
					+ "    c.cname\r\n"
					+ "FROM board b\r\n"
					+ "    LEFT JOIN category c ON b.cno = c.cno\r\n"
					+ "    LEFT JOIN member m ON b.mno = m.mno\r\n"
					+ "WHERE (\r\n"
					+ "        b.blat BETWEEN ? AND ?\r\n"
					+ "    )\r\n"
					+ "    AND (\r\n"
					+ "        b.blng BETWEEN ? AND ?\r\n"
					+ "    )\r\n"
					+ "    AND IF(? = 0, b.cno = b.cno, b.cno = ?)\r\n"
					+ "ORDER BY\r\n"
					+ "    CASE\r\n"
					+ "        WHEN ? = '최신순' THEN -b.bno\r\n"
					+ "        WHEN ? = '오래된순' THEN b.bno\r\n"
					+ "        WHEN ? = '거리순' THEN ST_DISTANCE_SPHERE(\r\n"
					+ "            POINT(?, ?),\r\n"
					+ "            POINT(\r\n"
					+ "                CAST(b.blng AS DOUBLE),\r\n"
					+ "                CAST(b.blat AS DOUBLE)\r\n"
					+ "            )\r\n"
					+ "        )\r\n"
					+ "    END asc\r\n"
					+ "limit ?, ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, qa);
			ps.setString(2, pa);
			ps.setString(3, ha);
			ps.setString(4, oa);
			ps.setInt(5, cno); 
			ps.setInt(6, cno);
			
			ps.setString(7, sSort); //정렬 기준
			ps.setString(8, sSort);
			ps.setString(9, sSort);
			
			ps.setDouble(10, clng); // 사용자 중심 좌표 , 중심 좌표 기준 거리순 출력을 위함
			ps.setDouble(11, clat);
			
			ps.setInt(12, start);
			ps.setInt(13, end);
			
			rs = ps.executeQuery();
			while(rs.next())
			{
				
				res.add(BoardDto.builder()
						.btitle(rs.getString("btitle"))
						.bno(rs.getInt("bno"))
						.bcontent(rs.getString("bcontent"))
						.bdate(rs.getString("bdate"))
						.mnickName(rs.getString("mnickname"))
						.cname(rs.getString("cname"))
						.mno(rs.getInt("mno"))
						.cno(rs.getInt("cno"))
						.mid(rs.getString("mid"))
						.blat(rs.getString("blat"))
						.blng(rs.getString("blng"))
						.build());
				
			}
			return res;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("좌표기준 게시글 가져오기 에러"+e);
		}
    	return null;
    }
    
    //3-1 페이지 확인
    public PageDto getPage(int cno,int nowpage,int viewByPage,String pa,String qa,String ha,String oa)
    {
    	try {
			String sql = "select t.cnt totalboard,Round(t.cnt/?) totalpage\r\n"
					+ "from (\r\n"
					+ "        select count(b.bno) cnt\r\n"
					+ "        from board b\r\n"
					+ "        where (\r\n"
					+ "                b.blat BETWEEN ? and ?\r\n"
					+ "            )\r\n"
					+ "            and (\r\n"
					+ "                b.blng BETWEEN ? and ?\r\n"
					+ "            )\r\n"
					+ "            and IF(? = 0,b.cno=b.cno, b.cno = ?)\r\n"
					+ "    ) as t";

			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, viewByPage);
			ps.setString(2, qa);
			ps.setString(3, pa);
			ps.setString(4, ha);
			ps.setString(5, oa);
			ps.setInt(6, cno);
			ps.setInt(7, cno);

			rs = ps.executeQuery();
			return rs.next() ? PageDto.builder().
					listsize(nowpage)
					.listsize(viewByPage)
					.cno(cno)
					.page(nowpage)
					.pa(pa).oa(oa).ha(ha).qa(qa)
					.totalsize(rs.getInt("totalboard"))
					.totalpage(rs.getInt("totalpage")).build() : null;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("getPage"+e);
		}
    	return null;
    }
    public ArrayList<CategoryDto> getCateList()
    {
    	try {
			String sql = "select * from category";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList<CategoryDto> res = new ArrayList<>();
			while(rs.next())
			{
				res.add(CategoryDto.builder().cno(rs.getInt(1)).cname(rs.getString(2)).build());
			}
			return res.size() > 1 ? res: null;
		} catch (Exception e) {
			// TODO: handle exceptions
			System.out.println("getCateList"+e);
		}
    	return null;
    }
    
    
    
    //4. 개별 출력 (dto 확인하기)
    public BoardDto boardInfo(int bno) {
    	
    	try {
			String sql = "select\r\n"
					+ "    b.bno, b.btitle ,b.bcontent ,b.bdate ,b.mno ,b.blat,b.cno ,b.blng ,m.mid,m.mnickname ,c.cname\r\n"
					+ "from board b\r\n"
					+ "    LEFT JOIN category c on b.cno = c.cno\r\n"
					+ "    LEFT JOIN member m on b.mno = m.mno\r\n"
					+ "where b.bno = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, bno);
			rs = ps.executeQuery();
			return rs.next() ? BoardDto.builder()
					.bcontent(rs.getString("bcontent"))
					.btitle(rs.getString("btitle"))
					.bdate(rs.getString("bdate"))
					.blat(rs.getString("blat"))
					.blng(rs.getString("blng"))
					.bno(rs.getInt("bno"))
					.cno(rs.getInt("cno"))
					.cname(rs.getString("cname"))
					.mid(rs.getString("mid"))
					.mno(rs.getInt("mno"))
					.mnickName(rs.getString("mnickname"))
					.build() : null;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("boardInfo "+e);
		}
    	
    	
    	
    	return null;
    
    
    }
    
  //4. mno으로 작성한 게시물 출력 
    public ArrayList<BoardDto> searchboard(int mno , int listsize , int startrow) {
    	
    	try {
			String sql = "select\r\n"
					+ "    b.bno, b.btitle ,b.bcontent ,b.bdate ,b.mno ,b.blat ,b.blng ,m.mnickname ,c.cname, c.cno\r\n"
					+ "from board b\r\n"
					+ "    LEFT JOIN category c on b.cno = c.cno\r\n"
					+ "    LEFT JOIN member m on b.mno = m.mno\r\n"
					+ "where m.mno = ? order by b.bdate desc limit ? , ? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, mno); ps.setInt( 2 , startrow); ps.setInt( 3 , listsize);
			rs = ps.executeQuery();
			ArrayList<BoardDto> boardDto = new ArrayList<>();
			while(rs.next())
			{
				boardDto.add(BoardDto.builder().bcontent(rs.getString("bcontent"))
						.btitle(rs.getString("btitle"))
						.bdate(rs.getString("bdate"))
						.blat(rs.getString("blat"))
						.blng(rs.getString("blng"))
						.bno(rs.getInt("bno"))
						.cname(rs.getString("cname"))
						.mno(rs.getInt("mno"))
						.mnickName(rs.getString("mnickname"))
						.cno(rs.getInt("cno"))
						.build());
				System.out.println(boardDto);
			}
			return boardDto;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("searchboard "+e);
		}
    	return null;
    
    
    }
    // mno로 조회한 게시물 수 
    public int getTotalSize(int mno ) {
    	System.out.println("getTotalSize 에 들어온 mno : "+ mno);
		try {
			String sql = "select count(*) from board b natural join member m where m.mno = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1,mno);
			rs = ps.executeQuery();
			if( rs.next() ) return rs.getInt(1);
		}catch (Exception e) { System.out.println("getTotalSize : " + e);}
		return 0;
	}
    
    
    
    //5. 삭제하기
    public boolean boardDelete(int bno,String mid) {
    	try {
			String sql = "delete from board where bno = ? and mno = (select mno from member where mid = ?)";
			ps =conn.prepareStatement(sql);
			ps.setInt(1, bno);
			ps.setString(2, mid);
			return 1 == ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("boardDelete : "+e);
		}
    	return false;
    }
    
    //6. 채팅방 생성여부
    public boolean exist(int bno) {
    	try {String sql = "select * from board where bno = ? and cattingexist=0";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, bno);
    		rs=ps.executeQuery();
    		while(rs.next()) {
    			return true;
    		}
			
		} catch (Exception e) {System.out.println("exist"+e);}
    	return false;
    }
    //7. 채팅방 상태 
    public boolean state(int bno) {
    	try {String sql = "update board set cattingexist=1 where bno = ?;";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, bno);
    		int rs=ps.executeUpdate();
    		if(rs==1) {
    			return true;
    		}
			
		} catch (Exception e) {System.out.println("state"+e);}
    	return false;
    }
    
    //8. 채팅방 방장 찾기
    public boolean host (String mid,int bno) {
		System.out.println("mid : " +mid);
    	try {String sql = "select * from board b natural join member m where b.mno=m.mno and  m.mid=? and b.bno=? ";
    		ps=conn.prepareStatement(sql);
    		ps.setString(1, mid);
    		ps.setInt(2, bno);
    		rs=ps.executeQuery();
    		if(rs.next()) {
    		
    			return true;
    		}
    	
		} catch (Exception e) {System.out.println("host : "+e);}
    	return false;
    }
    public boolean close(int bno) {
    	try {String sql = "update board set cattingexist=0 where bno = ?;";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, bno);
    		int rs=ps.executeUpdate();
    		if(rs==1) {
    			return true;
    		}
			
		} catch (Exception e) {System.out.println("state"+e);}
    	return false;
    }

    // 9. 카테고리 정보 가져오기
    public ArrayList<CategoryDto> getCategoryList() {
    	
    	ArrayList<CategoryDto> list = new ArrayList<>();
    	
    	try {
    		String sql = "select * from category";
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()) {
				CategoryDto dto = CategoryDto.builder().cno(rs.getInt("cno")).cname(rs.getString("cname"))
						.build();
				list.add(dto);
			}
    	} catch (Exception e) {System.out.println("getCategoryList : "+e);}
    	
		return list;
    	
    }
    
    // 10.차트 통계
    public ArrayList<String> getPostChart(String pa,String qa,String ha,String oa,String std,int cno)
    {

    	ArrayList<String> _c = new ArrayList<>();
    	try {
			String sql = "SELECT DATE_FORMAT(b.bdate, ?) cd, COUNT(b.bno) cnt FROM board b \r\n"
					+ "					    where ( b.blat BETWEEN ? and ?) \r\n"
					+ "					        and\r\n"
					+ "					        (b.blng BETWEEN ? and ?)\r\n"
					+ "                            and\r\n"
					+ "                            IF(? = 0,b.cno=b.cno, b.cno = ?)\r\n"
					+ "					GROUP BY DATE_FORMAT(b.bdate, ?) order by cd";
			ps = conn.prepareStatement(sql);
			ps.setString(1, std);
			ps.setString(2, qa);
			ps.setString(3, pa);
			ps.setString(4, ha);
			ps.setString(5, oa);
			ps.setInt(6, cno);
			ps.setInt(7, cno);
			ps.setString(8, std);
			
			rs = ps.executeQuery();
			while(rs.next())
			{
				_c.add(rs.getString(1)+","+ rs.getInt(2));
			}
			return _c;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("차트 에러"+e);
		}
    	
    	return _c;
    }

}
