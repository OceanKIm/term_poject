package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class HanoiTower extends JFrame implements Runnable{
	 // 메인 클래스 변수
	static HanoiTower game;
	
	// 맨 바닥 레이블.
	JLabel bottomLable = new JLabel();
	long start, end;
	boolean flag = true;
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
	String time = "                                   Are you ready?....."; // 시간 표현
	String space = "                "; // 공간
	String messages = "        "; // 메세지
	
	// 시행횟수.
	int countMove = 0;
	
	// start
	boolean isStart = false;

	// 노드구성
	JLabel node1 = new JLabel();
	
	// 아이콘 이미지 
	Image IconImg = Toolkit.getDefaultToolkit().createImage("./src/img/iconImg.jpg");	
	
	// 메인 그래픽 구성
	static ShowGraphics show = new ShowGraphics();
	
	// 쓰레드
	Thread timeThread = new Thread(this);
	boolean isThreadStart = false;
	
	public HanoiTower() {
		setTitle("하노이타워게임");
		setBounds(0,0,800,500);
		
		// 게임 아이콘
		setIconImage(IconImg);
		
		// 메인 그래픽 추가
		this.add(show);
		
		
		// 맨 바닥 레이블 추가
		Color c1 = new Color(0x5599CCFF, true);
		bottomLable.setOpaque(true);
		bottomLable.setPreferredSize(new Dimension(800,30));
		bottomLable.setBackground(c1);
		bottomLable.setText(time + space + messages);
		bottomLable.setHorizontalAlignment(JLabel.LEFT);
		bottomLable.setFont(new Font("고딕", Font.BOLD, 30));
		this.add(bottomLable, BorderLayout.SOUTH);
		
		JOptionPane.showMessageDialog(null, "하노이 타워 게임을 시작합니다.\n"
				                          + "조작법 : 방향키로 노드를 옮길 수 있습니다.\n"
				                          + "규칙 1 : 작은 노드위에 큰노드를 쌓을 수 없습니다.\n"
				                          + "규칙 2 : 중앙바에 타워를 다시 쌓아야 게임에 승리합니다.\n");
		
		// 방향키 설정
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
				
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				switch(e.getKeyCode()) {
				
				case KeyEvent.VK_RIGHT:
					System.out.println("==========================RIGHT!!");
					if (ShowGraphics.markloc != 570) {
						ShowGraphics.markloc += ShowGraphics.sideMove;
					}
					for (int i = 0; i < show.node.length; i++) {
						if (show.node[i].isUP == true) {
							if (ShowGraphics.markloc != 570 || show.node[i].x < 470) show.node[i].x += 200;
							show.node[i].nowSideloc = ShowGraphics.markloc;
						} 
					}
					show.repaint();
					break;
					
				case KeyEvent.VK_LEFT:
					System.out.println("==========================LEFT!!");
					if (ShowGraphics.markloc != 170) {
						ShowGraphics.markloc -= ShowGraphics.sideMove;
					}
					for (int i = 0; i < show.node.length; i++) {
						if (show.node[i].isUP == true) {
							if (ShowGraphics.markloc != 170 || show.node[i].x > 170) show.node[i].x -= 200;
							show.node[i].nowSideloc = ShowGraphics.markloc;
						} 
					}
					show.repaint();
					break;
					
				case KeyEvent.VK_UP:
					// 쓰레드 시작
					if (!isThreadStart) {
						timeThread.start();
						messages = "        Try COUNT = " + countMove;
						isThreadStart = true;
					}
				
					// 재업 방지
					if (checkUp()) {
						System.out.println("중복 업!!");
						break;
					}
					System.out.println("==========================UP!!");
					for (int i = 0; i < show.node.length; i++) {
						if (show.node[i].isHigher == true && show.node[i].nowSideloc == ShowGraphics.markloc) {
							show.node[i].y = ShowGraphics.uploc;
							System.out.println(i + "번 노드를 UP 시켰습니다.");
							show.node[i].isUP = true;	
							
							// 현재 위치했던 bar를 null 값으로 바꿔주기. 
							for (int j = 0; j < 3; j++) {
								if (show.bar[j].loc == show.node[i].nowSideloc) {
									System.out.println((j + 1) + "바 위치");
									for (int k = 0; k < show.bar[j].barLayer.length; k++) {
										System.out.println(k + "번 레이어");
										if(show.bar[j].barLayer[k] != null) {
											show.bar[j].barLayer[k] = null;
											System.out.println((j + 1) + "번" + k + "바레이어에 null 지정");
											break;
										}
									}
									break;
								}
							}		
							break;
						} 
						
				
					}
					
					
					// 하이어 노드 지정
					makeHigher();
					
					show.repaint();
					break;
					
				case KeyEvent.VK_DOWN:
					System.out.println("==========================DOWN!!");
					for (int i = 0; i < show.node.length; i++) {
						if (show.node[i].isUP == true && show.node[i].nowSideloc == ShowGraphics.markloc) {
							for (int j = 0; j < 3; j++) {
								if (show.bar[j].loc == show.node[i].nowSideloc) {
									for (int k = 5; k >= 0; k--) {
										
										// 큰 노드가 작은 노드 위에 쌓게하지 못하는 가장 핵심적인 규칙 추가.
										if (k < 5 && show.bar[j].barLayer[k] == null) {
											if (show.node[i].nodeNo > show.bar[j].barLayer[k + 1].nodeNo) {
												System.out.println("쌓을 수 없습니다..");
												messages = " Cannot be stacked...";
												break;
											}
										}
										
										
										if (show.bar[j].barLayer[k] == null) {
											
											System.out.println((j + 1) + "번" + k + "바의 위치");
											
											show.node[i].y = ShowGraphics.layer[k];
											checkMove(); // 동작 카운트
											System.out.println(i + "번 노드를 DOWN 시켰습니다.");
											show.bar[j].barLayer[k] = show.node[i];
											
											// isHigher를 반드시 false로 만들어야 함.
											show.node[i].isHigher = false;
										
											
											show.node[i].isUP = false;
											
											if (show.bar[j].barLayer[k] != null) {
												System.out.println((j + 1)+ "번" + k +"바레이어에 코드 삽입 완료.");
											}
											
											break;
											
										}
									}
									
								}
							}
							
							//show.node[i].y += 200;
							//show.node[i].isUP = false;
						} 
					}
					// 하이어 노드 재지정.
					makeHigher();
					
					// 게임 승리 확인
					if (isWin()) {
						timeThread.stop();
						JOptionPane.showMessageDialog(null, "축하합니다. 게임에서 승리했습니다.\n 시행횟수 : " + countMove + "\n" + time);
						System.out.println("게임 승리!!!!!!");
					}
					show.repaint();
					break;
				
					case KeyEvent.VK_SPACE:
					checkIshigher();
					//checkNodeNo();
					break;
					
					case KeyEvent.VK_R:
						System.out.println("개임 재시작.");
						game = null;
					break;
				}
			}
		});
		
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);		
	}
	
	
	public static void main(String[] args) {
		
		game = new HanoiTower();
		
	}
	
	// 각 바에서 맨위 노드를 하이 노드로 정해주는 메소드. 
	static void makeHigher() {
		// 바 1 하이어 지정
		boolean check1 = false;
		for (int i = 0; i < show.bar[0].barLayer.length; i++) {
			if (show.bar[0].barLayer[i] != null && check1 == false) {
				show.bar[0].barLayer[i].isHigher = true;
				System.out.println("1번" + i + "바레이어에 하이어 지정 완료.");
				check1 = true;
			} else if (show.bar[0].barLayer[i] != null){
				show.bar[0].barLayer[i].isHigher = false;
			}
		}
		// 바 2 하이어 지정
		boolean check2 = false;
		for (int i = 0; i < show.bar[1].barLayer.length; i++) {
			if (show.bar[1].barLayer[i] != null && check2 == false) {

				show.bar[1].barLayer[i].isHigher = true;
				System.out.println("2번" + i + "바레이어에 하이어 지정 완료.");
				check2 = true;
			} else if (show.bar[1].barLayer[i] != null){
				show.bar[1].barLayer[i].isHigher = false;
			}
		}
		// 바 3 하이어 지정
		boolean check3 = false;
		for (int i = 0; i < show.bar[2].barLayer.length; i++) {
			if (show.bar[2].barLayer[i] != null && check3 == false) {
					
				show.bar[2].barLayer[i].isHigher = true;
				System.out.println("3번" + i + "바레이어에 하이어 지정 완료.");
				check3 = true;
			}else if (show.bar[2].barLayer[i] != null){
				show.bar[2].barLayer[i].isHigher = false;
			}
		}
		
	}	
	
	// isHigher 상태 보기 메서드
	static void checkIshigher() {
		
		for (int i = 0; i < show.node.length; i++) {
			if (show.node[i].isHigher == true) {
				System.out.println(i + "번 노드는 higher True 입니다.");
			} else {
				System.out.println(i + "번 노드는 higher false 입니다.");
			}
		}
		
	}
	
	// 노드 넘버 찍어보기
	static void checkNodeNo() {
		for (int i = 0; i < show.node.length; i++) {
			System.out.println("node1의 넘버 : " + show.node[i].nodeNo);
		}
	}
	
	// 이미 업이 있는 상태에서 재업시키기 금지.
	static boolean checkUp() {
		for (int i = 0; i < show.node.length; i++) {
			if (show.node[i].isUP == true) {
				return true;
			}
		}
		return false;
	}
	
	// 게임 승리 결과를 알려주는 메서드
	public boolean isWin() {
		System.out.println("checkWin__1");
		if (show.bar[1].barLayer[0] != null || show.bar[2].barLayer[0] != null) {
			System.out.println("checkWin__2");
			try {
				if (show.bar[1].barLayer[0].nodeNo == 1 || show.bar[2].barLayer[0].nodeNo == 1) {
					System.out.println("checkWin__3");
					return true;
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "실패했습니다!! 중앙에 탑을 다시 쌓으세요!!!");
			}
		}
		return false;
	}
	
	// 시행횟수 카운트
	public void checkMove() {
		countMove++;
		messages = "        Try COUNT = " + countMove;
	}
	
	@Override
	public void run() {
		
		if (start == 0) {
			start = System.currentTimeMillis();
			end = start;
		}
		while(true) {
			long Time = end++ -start - 32400000;
			this.time = " Time = " + sdf.format(Time);
			bottomLable.setText(time + space + messages);
			if(!flag) {
				break;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
} // class


















