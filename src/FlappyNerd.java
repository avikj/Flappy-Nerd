// haljkds;fja;lksdjf;lakjsl;dkf gagan is a beast
import javax.swing.*; //import required libraries
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.geom.*;

public class FlappyNerd extends JFrame {
	private String actualasdfingurl = null;
	String quizletUrl;
	public FlappyNerd(String quizletUrl) {
		this.quizletUrl = quizletUrl;
		setLayout(null);
		setTitle("Flappy Nerd");
		setSize(1200, 800);
		setLocation(100, 100);
		setResizable(false);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setContentPane(new GamePanel(-1, "EasyHiScores.txt"));
		setVisible(true);
	}

	class GamePanel extends JPanel implements MouseListener, FocusListener,
			KeyListener {
		int pipeSpacing = 700;
		private int theme = 1;
		private int JUMPVEL = -11;
		private String scorefilename = "EasyHiScores.txt";
		private Color[] birdColors = { Color.RED, new Color(170, 0, 0) };
		private Color[] beakColors = { Color.YELLOW, Color.YELLOW };
		private Color[] backgroundColors = { Color.CYAN, Color.DARK_GRAY };
		private Color[] pipeColors = { Color.GREEN, Color.RED };
		private Color[] cloudColors = { Color.WHITE, Color.BLACK };
		private Color[] groundColors = { new Color(75, 30, 15), Color.BLACK };
		private Color[] scoreColors = { Color.BLACK, Color.WHITE };
		int[] treeHeights = new int[20];
		Rectangle[][] pipeArray = new Rectangle[3][100];
		Color birdColor = Color.RED;
		int birdx = 80;
		int birdy = 300;
		boolean starting = false;
		boolean started = false;
		boolean dead = false;
		boolean scoreSubmitted = false;
		Rectangle birdRect;
		double yvelocity = JUMPVEL;
		int xvelocity = 7; // was 6
		int scrollVelocity = xvelocity;
		int framesPassed = 0;
		int score = 0;
		int oldScore = -1;
		Robot bot;

		final String PI = "\u03C0";
		final String SQRT = "\u221A";
		final String DIV = "/";
		final String SQUARED = "\u00B2";
		final String THETA = "\u03B8";
		final String PLUSMINUS = "\u00B1";
		final String MINUSPLUS = "\u2213";

		JTextField nameField = new JTextField("Your Name");
		Timer back = new Timer(22, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (framesPassed > 5)
					framesPassed -= 5 * (int) Math.sqrt(score + 1);
				else {
					back.stop();
					reset(score);
				}
				repaint();
			}
		});

		String[][] expressions = QuizletArray.getShit(quizletUrl);

		Rectangle[] wrongRects = new Rectangle[100];
		Rectangle[] rightRects = new Rectangle[100];
		boolean[] correctGaps = new boolean[100];
		int[] equationIndexes = new int[100];

		Timer gameTimer;
		int homeBirdx = 0;
		int homeBirdy = 300;

		JButton easyButton = new JButton("Med.");
		JButton mediumButton = new JButton("Hard");
		JButton hardButton = new JButton("GG");
		JButton startButton = new JButton("Start");
		Timer t = new Timer(1, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				homeBirdx += 10;
				if (homeBirdx > 1200)
					homeBirdx -= 1200;
				if (homeBirdx % 175 == 0)
					yvelocity *= -1;
				if (homeBirdx == 80 || homeBirdx == 1280) {
					if (starting) {
						gameTimer.start();
						started = true;
						yvelocity = -10;
						t.stop();
					}
				}
				if (scorefilename.equals("EasyHiScores.txt")) {
					easyButton.setBackground(Color.GRAY);
				} else if (scorefilename.equals("MediumHiScores.txt")) {
					mediumButton.setBackground(Color.GRAY);
				} else if (scorefilename.equals("HardHiScores.txt")) {
					hardButton.setBackground(Color.GRAY);
				}
				repaint(0, 200, 1900, 400);
			}
		});

		public GamePanel(int s, String oldfilename) {
			scorefilename = oldfilename;
			if (scorefilename.equals("EasyHiScores.txt")) {
				easyButton.setBackground(Color.GRAY);
				pipeSpacing = 700;

			} else if (scorefilename.equals("MediumHiScores.txt")) {
				pipeSpacing = 550;
				mediumButton.setBackground(Color.GRAY);
			} else if (scorefilename.equals("HardHiScores.txt")) {
				pipeSpacing = 400;
				hardButton.setBackground(Color.GRAY);
			}
			try {
				bot = new Robot();
			} catch (Exception e) {
			}

			initializeData();
			setLayout(null);
			setFocusable(true);
			addMouseListener(this);
			addKeyListener(this);
			addFocusListener(this);
			requestFocus();
			oldScore = s;
			UIManager.put("Button.select", Color.GRAY);
			gameTimer = new Timer(20, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					bot.mouseMove(MouseInfo.getPointerInfo().getLocation().x,
							MouseInfo.getPointerInfo().getLocation().y); // use
																			// Robot
																			// to
																			// move"
																			// mouse
																			// to
																			// current
																			// location(to
																			// make
																			// display
																			// less
																			// choppy)
					yvelocity += 0.78;// decrease upwards velocity - originally
										// .85
					birdy += yvelocity;// decrease position by velocity
					birdx += xvelocity;

					for (int i = 0; i < pipeArray.length; i++) {
						for (int j = 0; j < pipeArray[i].length; j++) {
							if (birdRect.intersects(pipeArray[i][j])) {
								dead = true;
							}
						}
					}
					for (int i = 0; i < wrongRects.length; i++) {
						if (birdRect.intersects(wrongRects[i])) {
							dead = true;
						}
					}
					if (birdy > 685) {
						dead = true;
						gameTimer.stop();
						back.start();// reset(score);
					}
					if (!dead)
						framesPassed++;

					easyButton.setBounds(250, 406 + 25 * framesPassed, 50, 50);
					mediumButton
							.setBounds(316, 406 + 25 * framesPassed, 50, 50);
					hardButton.setBounds(383, 406 + 25 * framesPassed, 50, 50);
					nameField.setBounds(250, 455 + 25 * framesPassed, 200, 30);

					startButton
							.setBounds(250, 425 + 25 * framesPassed, 181, 50);
					score = (birdx - 1500 + pipeSpacing) / pipeSpacing;
					if (score < 0)
						score = 0;
					repaint();
				}
			});
			// gameTimer.start();
			setLayout(null);
			// helpButton.setBounds(780, 210, 20, 20);
			easyButton.setBackground(Color.RED);
			easyButton.setToolTipText("Choose a difficulty.");
			easyButton.setForeground(Color.WHITE);
			easyButton.setFont(new Font("asdf", Font.BOLD, 20));
			easyButton.setBounds(250, 406, 61, 50);
			easyButton.setFocusable(false);
			easyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			easyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					scorefilename = "EasyHiScores.txt";
					pipeSpacing = 700;
					initializeData();

					hardButton.setBackground(Color.RED);
					easyButton.setBackground(Color.GRAY);
					mediumButton.setBackground(Color.RED);
				}
			});

			mediumButton.setBackground(Color.RED);
			mediumButton.setToolTipText("Choose a difficulty.");
			mediumButton.setForeground(Color.WHITE);
			mediumButton.setFont(new Font("asdf", Font.BOLD, 20));
			mediumButton.setBounds(310, 406, 61, 50);

			mediumButton.setFocusable(false);
			mediumButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			mediumButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					scorefilename = "MediumHiScores.txt";
					pipeSpacing = 550;
					initializeData();

					mediumButton.setBackground(Color.GRAY);
					easyButton.setBackground(Color.RED);
					hardButton.setBackground(Color.RED);
				}
			});

			hardButton.setBackground(Color.RED);
			hardButton.setToolTipText("Choose a difficulty.");
			hardButton.setForeground(Color.WHITE);
			hardButton.setFont(new Font("asdf", Font.BOLD, 20));
			hardButton.setFocusable(false);
			hardButton.setBounds(370, 406, 61, 50);
			hardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			hardButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					scorefilename = "HardHiScores.txt";
					pipeSpacing = 400;
					initializeData();
					hardButton.setBackground(Color.GRAY);
					easyButton.setBackground(Color.RED);
					mediumButton.setBackground(Color.RED);
				}
			});

			startButton.setBackground(Color.RED);
			startButton.setForeground(Color.WHITE);
			startButton.setToolTipText("Start the game!");
			startButton.setFont(new Font("asdf", Font.BOLD, 20));
			startButton.setBounds(250, 455, 181, 50);
			startButton.setFocusable(false);
			startButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					

					GamePanel.this.requestFocus();
				}
			});
			nameField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			nameField.setFont(new Font("asdf", Font.BOLD, 20));
			nameField.setBounds(250, 379, 181, 30);
			nameField.setToolTipText("Enter your name and press \'Enter\'");
			final String oldfilenamecopy = oldfilename;
			nameField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!nameField.getText().equals("") && !scoreSubmitted) {
						scoreSubmitted = true;
						GamePanel.this.remove(nameField);
						if (oldfilenamecopy.equals("MediumHiScores.txt")) {
							easyButton.setBackground(Color.RED);
							mediumButton.setBackground(Color.GRAY);
							hardButton.setBackground(Color.RED);
						} else if (oldfilenamecopy.equals("EasyHiScores.txt")) {
							easyButton.setBackground(Color.GRAY);
							mediumButton.setBackground(Color.RED);
							hardButton.setBackground(Color.RED);
						} else if (oldfilenamecopy.equals("HardHiScores.txt")) {
							easyButton.setBackground(Color.RED);
							mediumButton.setBackground(Color.RED);
							hardButton.setBackground(Color.GRAY);
						}
					}
				}
			});
			/*
			 * if (oldScore >= 0) add(nameField); add(easyButton);
			 * add(mediumButton); add(hardButton);
			 */
			//add(startButton);
			t.start();
			requestFocus();
		}

		public void paintComponent(Graphics g) {
			setBackground(new Color(94, 254, 238));
			super.paintComponent(g);
			g.translate(-scrollVelocity * framesPassed, 0);
			birdRect = new Rectangle(birdx - 20, birdy - 15, 40, 30);
			drawClouds((int) (scrollVelocity * framesPassed * 0.9), g);
			// drawGround(0.4, 30, g);
			// drawGround(0.3, 20, g);
			drawTrees(treeHeights,
					(int) (scrollVelocity * framesPassed * 0.2 + 1500), g);
			drawGround(0.2, 10, g);
			drawPipes(g);
			if (started)
				drawBird(birdx, birdy, g.create());
			// drawGround(-0.1, -10, g);

			g.setFont(new Font("asdf", Font.BOLD, 140)); // display title text
			int titleX = 100;
			int titleY = -framesPassed * 25 + 150;

			String title = "Flappy Nerd";
			g.setColor(Color.BLACK);
			g.drawString(title, titleX + 4, titleY);
			g.drawString(title, titleX - 4, titleY);
			g.drawString(title, titleX, titleY + 4);
			g.drawString(title, titleX, titleY - 4);
			g.setColor(Color.WHITE);
			g.drawString(title, titleX, titleY);


			

			if (started) { // display score
				int scorex = 470 + scrollVelocity * framesPassed;
				int scorey = 100;
				g.setFont(new Font("", Font.BOLD, 100));
				drawOutlinedString(score + "", scorex, scorey, Color.WHITE,
						Color.BLACK, g, 2);
			}

			if (!started) {
				drawBird(homeBirdx, homeBirdy, g);
				drawBird(homeBirdx - 1200, homeBirdy, g);
			}
			if (dead)
				xvelocity = 0;

		}

		public void drawGrass(Graphics g) {

		/*	Color c = new Color(0, 235, 0);
			g.setColor(c);
			// g.fillRect(500,700,50,20);
			for (int x = 0; x < correctGaps.length; x++) {
				if (correctGaps[x])
					g.fillRect(pipeArray[2][x].x + 90, 680, 10, 50);
				else
					g.fillRect(pipeArray[2][x].x, 680, 10, 50);

			}*/

		}

		public void initializeData() {
			for (int i = 0; i < correctGaps.length; i++) {
				if (Math.random() < 0.5)
					correctGaps[i] = true;
				else
					correctGaps[i] = false;
			}

			for (int i = 0; i < equationIndexes.length; i++) {
				equationIndexes[i] = (int) (Math.random() * expressions.length);
			}

			for (int i = 0; i < pipeArray[0].length; i++) {
				int shift = (int) (Math.random() * 100) - 50;
				pipeArray[0][i] = new Rectangle(i * pipeSpacing + 1500, -500
						+ shift, 100, 635);
				pipeArray[1][i] = new Rectangle(i * pipeSpacing + 1500,
						315 + shift, 100, 100);
				pipeArray[2][i] = new Rectangle(i * pipeSpacing + 1500,
						595 + shift, 100, 1000);
				if (correctGaps[i]) {
					wrongRects[i] = new Rectangle(i * pipeSpacing + 1500,
							135 + shift, 100, 180);
					rightRects[i] = new Rectangle(i * pipeSpacing + 1500,
							415 + shift, 100, 180);
				} else {
					wrongRects[i] = new Rectangle(i * pipeSpacing + 1500,
							415 + shift, 100, 180);
					rightRects[i] = new Rectangle(i * pipeSpacing + 1500,
							135 + shift, 100, 180);
				}
			}

			for (int i = 0; i < treeHeights.length; i++)
				treeHeights[i] = (int) (Math.random() * 300 + 300);
		}

		public void drawClouds(int x, Graphics g) {
			for (int i = 0; i < 10000; i += 170) {
				g.setColor(Color.BLACK);
				g.fillOval(i + x - 1, -45, 230 + 2, 90 + 2);
			}
			for (int i = 0; i < 10000; i += 170) {
				g.setColor(Color.WHITE);
				g.fillOval(i + x, -45, 230, 90);
			}
		}

		public void drawTrees(int[] trees, int x, Graphics g) {
			for (int i = 0; i < trees.length; i += 1) {

				g.setColor(new Color(101, 51, 0));
				g.fillRect(i * pipeSpacing + x + 20, trees[i] + 10, 30, 700);
				g.setColor(Color.BLACK);
				g.drawRect(i * pipeSpacing + x + 20, trees[i] + 10, 30, 700);
				g.setColor(Color.BLACK);
				g.fillOval(i * pipeSpacing + x - 1, trees[i] - 1, 70 + 2, 70 + 2);
				g.fillOval(i * pipeSpacing + x - 1, trees[i] + 25 - 1, 70 + 2, 70 + 2);
				g.fillOval(i * pipeSpacing + x + 20 - 1, trees[i] + 15 - 1, 70 + 2, 70 + 2);
				g.fillOval(i * pipeSpacing + x - 20 - 1, trees[i] + 15 - 1, 70 + 2, 70 + 2);
				g.fillOval(i * pipeSpacing + x - 30 - 1, trees[i] + 25 - 1, 70 + 2, 70 + 2);
				g.fillOval(i * pipeSpacing + x + 30 - 1, trees[i] + 25 - 1, 70 + 2, 70 + 2);
				g.setColor(new Color(0, 160, 0));
				g.fillOval(i * pipeSpacing + x, trees[i], 70, 70);
				g.fillOval(i * pipeSpacing + x, trees[i] + 25, 70, 70);
				g.fillOval(i * pipeSpacing + x + 20, trees[i] + 15, 70, 70);
				g.fillOval(i * pipeSpacing + x - 20, trees[i] + 15, 70, 70);
				g.fillOval(i * pipeSpacing + x - 30, trees[i] + 25, 70, 70);
				g.fillOval(i * pipeSpacing + x + 30, trees[i] + 25, 70, 70);

			}
		}

		public void drawGround(double shift, int y, Graphics g) {
			int x = (int) (scrollVelocity * framesPassed * shift);
			g.setColor(new Color(5, 190, 5));
			g.fillRect(-100 + x, 720 - y, 30000, 21);
			g.setColor(new Color(50, 170, 0));
			for (int i = -100; i < 30000; i += 80) {
				for (int j = 0; j < 4; j++)
					g.drawLine(i + j + x, 920 - y, i - 20 + j + x, 740 - y);// g.fillRect(i,520,14,28);
			}
			g.setColor(Color.BLACK);
			g.drawRect(-100 + x, 720 - y, 30000, 21);
			g.setColor(new Color(101, 51, 0));
			g.fillRect(-100 + x, 740 - y, 30000, 1000);
			g.setColor(Color.BLACK);
			g.drawRect(-100 + x, 740 - y, 30000, 1000);
		}

		public void drawBird(int x, int y, Graphics gc) {

			Graphics2D g = (Graphics2D) gc;
			if (started)
				g.transform(AffineTransform.getRotateInstance(yvelocity * 0.03,
						birdRect.x + birdRect.width / 2 - 5, birdRect.y
								+ birdRect.height / 2));
			// g.translate(-scrollVelocity*framesPassed, 0);;;;;
			g.setColor(birdColors[theme]);
			g.setColor(Color.RED);
			g.fillRoundRect(x - 20, y - 15, 40, 30, 16, 16);
			g.setColor(Color.BLACK);
			g.drawRoundRect(x - 20, y - 15, 40, 30, 16, 16);
			g.fillOval(x + 12, y - 10, 3, 3);

			int[] xs = { x - 10, x, x + 10 };// wing

			int[] ys = { y - 1, y + 10, y - 1 };
			if (yvelocity > 0)
				ys = new int[] { y + 1, y - 10, y + 1 }; // determine triangle
															// based on whether
															// bird is going up
															// or down
			g.drawPolygon(xs, ys, 3);

			xs = new int[] { x + 20, x + 35, x + 20 };// beak
			ys = new int[] { y - 6, y, y + 6 };
			g.setColor(beakColors[theme]);
			g.fillPolygon(xs, ys, 3);
			g.setColor(Color.BLACK);
			g.drawPolygon(xs, ys, 3);

		}

		public void drawPipes(Graphics g) {
			Rectangle r;
			for (int i = 0; i < pipeArray.length; i++) {
				for (int j = 0; j < pipeArray[i].length; j++) {
					r = pipeArray[i][j];
					g.setColor(Color.GREEN);// pipe rectangle
					g.fillRect((int) r.getX(), (int) r.getY(),
							(int) r.getWidth(), (int) r.getHeight());
					g.setColor(Color.BLACK);
					g.drawRect((int) r.getX(), (int) r.getY(),
							(int) r.getWidth(), (int) r.getHeight());

					g.setColor(Color.GREEN);// lip at top of pipe
					g.fillRoundRect((int) r.getX() - 5, (int) r.getY(),
							(int) r.getWidth() + 10, 20, 10, 10);
					g.setColor(Color.BLACK);
					g.drawRoundRect((int) r.getX() - 5, (int) r.getY(),
							(int) r.getWidth() + 10, 20, 10, 10);

					g.setColor(Color.GREEN);// lip at bottom of pipe
					g.fillRoundRect((int) r.getX() - 5,
							(int) (r.getY() + r.getHeight() - 20),
							(int) r.getWidth() + 10, 20, 10, 10);
					g.setColor(Color.BLACK);
					g.drawRoundRect((int) r.getX() - 5,
							(int) (r.getY() + r.getHeight() - 20),
							(int) r.getWidth() + 10, 20, 10, 10);
				}
			}

			drawGrass(g);

			g.setFont(new Font("asdf", Font.BOLD, 40));
			for (int i = 0; i < rightRects.length; i++) {
				drawOutlinedString(expressions[equationIndexes[i]][1],
						rightRects[i].x, rightRects[i].y + 150, Color.WHITE,
						Color.BLACK, g, 2);
				drawOutlinedString(expressions[equationIndexes[i]][2],
						wrongRects[i].x, wrongRects[i].y + 150, Color.WHITE,
						Color.BLACK, g, 2);
				drawOutlinedString(expressions[equationIndexes[i]][0],
						rightRects[i].x, 720, Color.WHITE, Color.BLACK, g, 2);
			}

		}

		public void drawOutlinedString(String s, int x, int y, Color color,
				Color outline, Graphics g, int offset) {
			int strLength = s.length();
			s += " ";
			if (strLength > 15) {
				for (int asdf = 0; asdf < strLength / 15 + 1; asdf++) {
					String current = s;
					int currInd = 0;

					if (s.length() > 15 && s.contains(" "))
						current = s.substring(0, currInd = s.indexOf(" ", 14));

					g.setColor(outline);
					g.drawString(current, x + offset, y + 33 * asdf);
					g.drawString(current, x - offset, y + 33 * asdf);
					g.drawString(current, x, y + offset + 33 * asdf);
					g.drawString(current, x, y - offset + 33 * asdf);
					g.setColor(color);
					g.drawString(current, x, y + 33 * asdf);

					if (s.length() > 14)
						s = s.substring(currInd); // TODO
				}
			} else {
				g.setColor(outline);
				g.drawString(s, x + offset, y + 10);
				g.drawString(s, x - offset, y + 10);
				g.drawString(s, x, y + offset + 10);
				g.drawString(s, x, y - offset + 10);
				g.setColor(color);
				g.drawString(s, x, y + 10);
			}

		}

		public void reset(int s) {

			FlappyNerd.this.setContentPane(new GamePanel(s, scorefilename));
			FlappyNerd.this.revalidate();
		}

		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			if(!starting){
				initializeData();
				starting = true;
			}
			if (!dead && birdy > 50)
				yvelocity = JUMPVEL;
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if (!dead && birdy > 50)
				yvelocity = JUMPVEL;

		}

		public void keyReleased(KeyEvent e) {
		}
	}
	
}