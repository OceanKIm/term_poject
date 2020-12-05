package view;

import java.awt.Color;
import java.awt.Image;
import java.awt.Panel;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class Node {
	Color color;
	int x;
	int y;
	int nodeNo;
	static int count;
	
	int nowSideloc = 170;
	
	boolean isHigher = false;
	boolean isUP = false;
	
	Node nextNode;
	
	public Node(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
		nodeNo = ++count;
	}
}

class Bar {
	int loc;
	Node[] barLayer = new Node[6];
	public Bar(int loc) {
		this.loc = loc;
		for (int i = 0; i < barLayer.length; i++) {
			barLayer[i] = null;
		}
	}
}

public class ShowGraphics extends JPanel {

	Node[] node = {
			new Node(145,310, Color.BLUE),
			new Node(140,330, Color.GREEN),
			new Node(135,350, Color.ORANGE),
			new Node(130,370, Color.YELLOW),
			new Node(125,390, Color.PINK),
			new Node(120,410, Color.RED),	
	};
	// markloc 위치 지정자
	// markloc 위치 : 1. 170, 370, 570
	static int markloc = 170;
	static int marklocSub = 170;
	static int sideMove = 200;
	
	static int uploc = 110;
	
	// 레이어 설정
	static int[] layer = { 310, 330, 350, 370, 390, 410 };

	// 바 레이어 설정
	Bar[] bar = {
			new Bar(170),
			new Bar(370),
			new Bar(570)
	};
	
	// 노드 연결 구조를 생성자를 통해서 생성.
	public ShowGraphics() {	
		// 첫노드를 하이어 노드 설정
		node[0].isHigher = true;
			
		// 첫 바레이어에 각 노드 참조.
		for (int i = 0; i < node.length; i++) {
			bar[0].barLayer[i] = node[i];
		}
	}

	// 배경이미지 재삽입 시도
	Image img = new ImageIcon("./src/img/backGround.jpg").getImage();

	public void paint(java.awt.Graphics g) {

		// 배경이미지
		g.drawImage(img, -200, -100, null);
		
		// 바 삽입
		g.setColor(Color.GRAY);
		g.fill3DRect(185, 170, 20, 270, true);
		g.fill3DRect(385, 170, 20, 270, true);
		g.fill3DRect(585, 170, 20, 270, true);

		// 디스크 1 노드 
		g.setColor(node[0].color);
		g.fill3DRect(node[0].x, node[0].y, 100, 20, true);
		
		// 디스크 2 노드 
		g.setColor(node[1].color);
		g.fill3DRect(node[1].x, node[1].y, 110, 20, true);
		
		// 디스크 3 노드 
		g.setColor(node[2].color);
		g.fill3DRect(node[2].x, node[2].y, 120, 20, true);
		
		// 디스크 4 노드 
		g.setColor(node[3].color);
		g.fill3DRect(node[3].x, node[3].y, 130, 20, true);
		
		// 디스크 5 노드 
		g.setColor(node[4].color);
		g.fill3DRect(node[4].x, node[4].y, 140, 20, true);
		
		// 디스크 6 노드 
		g.setColor(node[5].color);
		g.fill3DRect(node[5].x, node[5].y, 150, 20, true);
		
		// 위치표시마크
		g.setColor(Color.magenta);
		g.fillArc(markloc, 50, 50, 50, 400, 100);
	}
}


























