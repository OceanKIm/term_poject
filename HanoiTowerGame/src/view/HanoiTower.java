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
	 // ���� Ŭ���� ����
	static HanoiTower game;
	
	// �� �ٴ� ���̺�.
	JLabel bottomLable = new JLabel();
	long start, end;
	boolean flag = true;
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
	String time = "                                   Are you ready?....."; // �ð� ǥ��
	String space = "                "; // ����
	String messages = "        "; // �޼���
	
	// ����Ƚ��.
	int countMove = 0;
	
	// start
	boolean isStart = false;

	// ��屸��
	JLabel node1 = new JLabel();
	
	// ������ �̹��� 
	Image IconImg = Toolkit.getDefaultToolkit().createImage("./src/img/iconImg.jpg");	
	
	// ���� �׷��� ����
	static ShowGraphics show = new ShowGraphics();
	
	// ������
	Thread timeThread = new Thread(this);
	boolean isThreadStart = false;
	
	public HanoiTower() {
		setTitle("�ϳ���Ÿ������");
		setBounds(0,0,800,500);
		
		// ���� ������
		setIconImage(IconImg);
		
		// ���� �׷��� �߰�
		this.add(show);
		
		
		// �� �ٴ� ���̺� �߰�
		Color c1 = new Color(0x5599CCFF, true);
		bottomLable.setOpaque(true);
		bottomLable.setPreferredSize(new Dimension(800,30));
		bottomLable.setBackground(c1);
		bottomLable.setText(time + space + messages);
		bottomLable.setHorizontalAlignment(JLabel.LEFT);
		bottomLable.setFont(new Font("���", Font.BOLD, 30));
		this.add(bottomLable, BorderLayout.SOUTH);
		
		JOptionPane.showMessageDialog(null, "�ϳ��� Ÿ�� ������ �����մϴ�.\n"
				                          + "���۹� : ����Ű�� ��带 �ű� �� �ֽ��ϴ�.\n"
				                          + "��Ģ 1 : ���� ������� ū��带 ���� �� �����ϴ�.\n"
				                          + "��Ģ 2 : �߾ӹٿ� Ÿ���� �ٽ� �׾ƾ� ���ӿ� �¸��մϴ�.\n");
		
		// ����Ű ����
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
					// ������ ����
					if (!isThreadStart) {
						timeThread.start();
						messages = "        Try COUNT = " + countMove;
						isThreadStart = true;
					}
				
					// ��� ����
					if (checkUp()) {
						System.out.println("�ߺ� ��!!");
						break;
					}
					System.out.println("==========================UP!!");
					for (int i = 0; i < show.node.length; i++) {
						if (show.node[i].isHigher == true && show.node[i].nowSideloc == ShowGraphics.markloc) {
							show.node[i].y = ShowGraphics.uploc;
							System.out.println(i + "�� ��带 UP ���׽��ϴ�.");
							show.node[i].isUP = true;	
							
							// ���� ��ġ�ߴ� bar�� null ������ �ٲ��ֱ�. 
							for (int j = 0; j < 3; j++) {
								if (show.bar[j].loc == show.node[i].nowSideloc) {
									System.out.println((j + 1) + "�� ��ġ");
									for (int k = 0; k < show.bar[j].barLayer.length; k++) {
										System.out.println(k + "�� ���̾�");
										if(show.bar[j].barLayer[k] != null) {
											show.bar[j].barLayer[k] = null;
											System.out.println((j + 1) + "��" + k + "�ٷ��̾ null ����");
											break;
										}
									}
									break;
								}
							}		
							break;
						} 
						
				
					}
					
					
					// ���̾� ��� ����
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
										
										// ū ��尡 ���� ��� ���� �װ����� ���ϴ� ���� �ٽ����� ��Ģ �߰�.
										if (k < 5 && show.bar[j].barLayer[k] == null) {
											if (show.node[i].nodeNo > show.bar[j].barLayer[k + 1].nodeNo) {
												System.out.println("���� �� �����ϴ�..");
												messages = " Cannot be stacked...";
												break;
											}
										}
										
										
										if (show.bar[j].barLayer[k] == null) {
											
											System.out.println((j + 1) + "��" + k + "���� ��ġ");
											
											show.node[i].y = ShowGraphics.layer[k];
											checkMove(); // ���� ī��Ʈ
											System.out.println(i + "�� ��带 DOWN ���׽��ϴ�.");
											show.bar[j].barLayer[k] = show.node[i];
											
											// isHigher�� �ݵ�� false�� ������ ��.
											show.node[i].isHigher = false;
										
											
											show.node[i].isUP = false;
											
											if (show.bar[j].barLayer[k] != null) {
												System.out.println((j + 1)+ "��" + k +"�ٷ��̾ �ڵ� ���� �Ϸ�.");
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
					// ���̾� ��� ������.
					makeHigher();
					
					// ���� �¸� Ȯ��
					if (isWin()) {
						timeThread.stop();
						JOptionPane.showMessageDialog(null, "�����մϴ�. ���ӿ��� �¸��߽��ϴ�.\n ����Ƚ�� : " + countMove + "\n" + time);
						System.out.println("���� �¸�!!!!!!");
					}
					show.repaint();
					break;
				
					case KeyEvent.VK_SPACE:
					checkIshigher();
					//checkNodeNo();
					break;
					
					case KeyEvent.VK_R:
						System.out.println("���� �����.");
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
	
	// �� �ٿ��� ���� ��带 ���� ���� �����ִ� �޼ҵ�. 
	static void makeHigher() {
		// �� 1 ���̾� ����
		boolean check1 = false;
		for (int i = 0; i < show.bar[0].barLayer.length; i++) {
			if (show.bar[0].barLayer[i] != null && check1 == false) {
				show.bar[0].barLayer[i].isHigher = true;
				System.out.println("1��" + i + "�ٷ��̾ ���̾� ���� �Ϸ�.");
				check1 = true;
			} else if (show.bar[0].barLayer[i] != null){
				show.bar[0].barLayer[i].isHigher = false;
			}
		}
		// �� 2 ���̾� ����
		boolean check2 = false;
		for (int i = 0; i < show.bar[1].barLayer.length; i++) {
			if (show.bar[1].barLayer[i] != null && check2 == false) {

				show.bar[1].barLayer[i].isHigher = true;
				System.out.println("2��" + i + "�ٷ��̾ ���̾� ���� �Ϸ�.");
				check2 = true;
			} else if (show.bar[1].barLayer[i] != null){
				show.bar[1].barLayer[i].isHigher = false;
			}
		}
		// �� 3 ���̾� ����
		boolean check3 = false;
		for (int i = 0; i < show.bar[2].barLayer.length; i++) {
			if (show.bar[2].barLayer[i] != null && check3 == false) {
					
				show.bar[2].barLayer[i].isHigher = true;
				System.out.println("3��" + i + "�ٷ��̾ ���̾� ���� �Ϸ�.");
				check3 = true;
			}else if (show.bar[2].barLayer[i] != null){
				show.bar[2].barLayer[i].isHigher = false;
			}
		}
		
	}	
	
	// isHigher ���� ���� �޼���
	static void checkIshigher() {
		
		for (int i = 0; i < show.node.length; i++) {
			if (show.node[i].isHigher == true) {
				System.out.println(i + "�� ���� higher True �Դϴ�.");
			} else {
				System.out.println(i + "�� ���� higher false �Դϴ�.");
			}
		}
		
	}
	
	// ��� �ѹ� ����
	static void checkNodeNo() {
		for (int i = 0; i < show.node.length; i++) {
			System.out.println("node1�� �ѹ� : " + show.node[i].nodeNo);
		}
	}
	
	// �̹� ���� �ִ� ���¿��� �����Ű�� ����.
	static boolean checkUp() {
		for (int i = 0; i < show.node.length; i++) {
			if (show.node[i].isUP == true) {
				return true;
			}
		}
		return false;
	}
	
	// ���� �¸� ����� �˷��ִ� �޼���
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
				JOptionPane.showMessageDialog(null, "�����߽��ϴ�!! �߾ӿ� ž�� �ٽ� ��������!!!");
			}
		}
		return false;
	}
	
	// ����Ƚ�� ī��Ʈ
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


















