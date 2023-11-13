package jspteam4.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jspteam4.model.Dto.BoardDto;

public class UserLogWriter {
	
	private static UserLogWriter instance = new UserLogWriter();
    public static UserLogWriter getInstance() {return instance;}
    private UserLogWriter() {};
    private static  Double EARTH_RADIUS = 6371 * 1 * (Math.PI / 180) * Math.cos(Math.toRadians(35.8448));
    
    public void BoardInfoViewLog(String logPath,String mid,BoardDto bdt) 
    {
    	//System.out.println(logPath);
    	
    	try {
    		FileOutputStream fos = new FileOutputStream(logPath+"/"+mid+".txt",true);
    		
    		String _log =  LocalDate.now()+":"
    				+bdt.getBlat()+":"
    				+bdt.getBlng()+":"
    				+bdt.getBno()+":"
    				+bdt.getCno()+"\n";
    				
    		fos.write(_log.getBytes());
    		fos.close();
		} catch (Exception e) {
			System.out.println("로그 저장 에러 ");
		}
    	
    }
    
    
    public int Get3RankBoard(String logPath,String mid,String nowLat,String nowLng)
    {
    	
    	ArrayList<BoardDto> _list = new ArrayList<>();
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(logPath+"/"+mid+".txt"));
    		while(true)
    		{
    			String line = br.readLine();
    			if(line == null)
    			{
    				break;
    			}
    			String[] _getline = line.split(":");
    			_list.add(BoardDto.builder()
    					.blat(_getline[1])
    					.blng(_getline[2])
    					.bno(Integer.parseInt(_getline[3]))
    					.cno(Integer.parseInt(_getline[4]))
    					.build());
    		}
    		ArrayList<ArrayList<BoardDto>> _3rankBoard = new ArrayList<>();
    		HashMap<Integer,Integer> _ctCnt = new HashMap<>() ; 
    		
    		_3rankBoard.add(new ArrayList<>());
    		_3rankBoard.add(new ArrayList<>());
    		_3rankBoard.add(new ArrayList<>());
    		for(BoardDto _b :_list)
    		{
    			//System.out.println(Double.parseDouble(_b.getBlat())+":"+Double.parseDouble(_b.getBlng()));
    			int _dist = (int)getDistance(Double.parseDouble(nowLat), Double.parseDouble(nowLng), Double.parseDouble(_b.getBlat()),Double.parseDouble(_b.getBlng()));
    			
    			if(_dist<300)
    			{
    				int level = Math.round(_dist/100);
    				_3rankBoard.get(level).add(_b);
    				if(_ctCnt.containsKey(_b.getCno()))
    				{
    					_ctCnt.put(_b.getCno(),_ctCnt.get(_b.getCno())+1);
    					
    				}
    				else
    				{
    					_ctCnt.put(_b.getCno(),0);
    				}
    			}
    		}
    		
    		 List<Map.Entry<Integer, Integer>> list = new ArrayList<>(_ctCnt.entrySet());
    		 Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
    	            @Override
    	            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
    	                return o2.getValue().compareTo(o1.getValue());
    	            }
    	        });
    		 List<Map.Entry<Integer, Integer>> topThree = list.subList(0, 3);

    		Double maxScore = Double.MAX_VALUE;
    		int savebno = 0;
    		for(int i=1;i<3;i++)
    		{
    			for(BoardDto bdt : _3rankBoard.get(i))
    			{
    				for(int ci = 0;ci<3;ci++)
    				{
    					if(bdt.getCno() == topThree.get(ci).getKey())
    					{

    						if((Math.pow(0.1, ci)*i) < maxScore)
    						{
    							maxScore = ci*(1*(Math.pow(0.1, i)));
    							savebno = bdt.getBno();
    						}
    						break;
    					}
    				}
    			}
    			
    		}

			return savebno;	
		} catch (Exception e) {
			System.out.println("추천 파일 에러 "+e);
		}
    	return 0;
    }
    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
    	  double dLat = Math.toRadians(lat2 - lat1);
    	  double dLon = Math.toRadians(lon2 - lon1);

    	  double a = Math.sin(dLat/2)* Math.sin(dLat/2)+ Math.cos(Math.toRadians(lat1))* Math.cos(Math.toRadians(lat2))* Math.sin(dLon/2)* Math.sin(dLon/2);
    	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    	  double d =EARTH_RADIUS* c * 10000;    // 두 지점 사이의 거리를 km환산
    	  return d;
    	}
}
