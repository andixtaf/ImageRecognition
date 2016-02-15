import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * MRBaseMain frame for the Multimedia Retrieval BTU Base System
 *
 * @author David Zellhoefer
 *
 */
public class MainFrame extends JFrame implements ActionListener {

	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem cmdFileChoose;
	private JMenuItem cmdExit;
	private JMenu menuAlgorithms, histogram;
	private JMenuItem intersect, L1Distance, histRGB, histHSI, histGray, histseggray, histsegrgb, histseghsi,
			segintersect, segl1dist, hsiintersect, hsil1dist, hsisegintersect, hsisegl1dist, hsieucdist,
			chisquaresemi, nra_algo;
	private JSplitPane splitter,  leftSplitter;
	private JPanel leftCmdPanel;
	private JScrollPane leftScrollPanel;
	private JScrollPane rightPanel;
	private JList listFiles,  listSorted;
	private JButton btnPreview;
	private Vector<MRImage> images = null;
	private JButton  segmentButton;
	private String imagePath1 = "";
	private Intersection intersection = null;
	private L1Distance l1distance = null;
	private Seg_Intersection segintersection = null;
	private Seg_L1Distance segl1distance = null;
	private HSI_Intersection hsiintersection = null;
	private HSI_L1Distance hsil1distance = null;
	private HSISeg_Intersection hsisegintersection = null;
	private HSISeg_L1Distance hsisegl1distance = null;
	private HSI_Euclidean_Distance hsieucliddist = null;
	private Chi_Square_Semi_Pseudo_Distance chisquare = null;
	private NRA_Algorithm nra_algorithm = null;
	private JFrame frame, frame1, frame2;
	private int segstep = 0;
	private int number = 0;
	private int similarity = 1;
	private JTextField tfield, tfield1;
	private Boolean seg, simi, l1dist, intersec, hsidist, hsiintersec, rgb, hsi, gray, eucl, nra;

	/**
	 * Constructs the main frame for the application
	 *
	 * @param arg0
	 *            The frame's title
	 * @param img
	 *            The image repository
	 * @param splash
	 *            The splash screen
	 * @throws HeadlessException
	 */
	public MainFrame(String arg0, SplashScreen splash, Thread splashThread)
			throws HeadlessException {
		super(arg0);
		images = loadTestData();
		if (images != null) {
			init();
			seg = false;
			simi = false;
			l1dist = false;
			intersec = false;
			hsidist = false;
			hsiintersec = false;
			rgb = false;
			hsi = false;
			gray = false;
			eucl = false;
			nra = false;
			splash.setMessage("Initializing GUI...");
			createGUI();
			splash.dispose();
			splashThread.stop();

		} else {
			JOptionPane.showMessageDialog(null, "Could not load test data", "Error", JOptionPane.ERROR_MESSAGE);
			splash.dispose();
			splashThread.stop();
			System.exit(1);
		}
	}

	/**
	 * Initializes the application, all non-GUI class creation should be done
	 * here
	 */
	private void init() {
		//Klassen zur Distanzberechnung initialisieren
		intersection = new Intersection();
		l1distance = new L1Distance();
		segintersection = new Seg_Intersection();
		segl1distance = new Seg_L1Distance();
		hsiintersection = new HSI_Intersection();
		hsil1distance = new HSI_L1Distance();
		hsisegintersection = new HSISeg_Intersection();
		hsisegl1distance = new HSISeg_L1Distance();
		hsieucliddist = new HSI_Euclidean_Distance();
		chisquare = new Chi_Square_Semi_Pseudo_Distance();
		nra_algorithm = new NRA_Algorithm();
	}

	/**
	 * Creates the GUI
	 */
	private void createGUI() {

		this.menuBar = new JMenuBar();

		// create menus
		menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		cmdFileChoose = new JMenuItem("Load Testdata");
		cmdFileChoose.addActionListener(this);

		menuFile.add(this.cmdFileChoose);

		cmdExit = new JMenuItem("Exit");
		cmdExit.addActionListener(this);
		menuFile.add(this.cmdExit);

		menuAlgorithms = new JMenu("Similarity Algorithms");
		this.menuAlgorithms.setMnemonic(KeyEvent.VK_S);
		intersect = new JMenuItem("Intersection");
		intersect.addActionListener(this);
		this.menuAlgorithms.add(this.intersect);
		L1Distance = new JMenuItem("L1-Distance");
		L1Distance.addActionListener(this);
		this.menuAlgorithms.add(this.L1Distance);
		hsiintersect = new JMenuItem("HSI-Intersection");
		hsiintersect.addActionListener(this);
		this.menuAlgorithms.add(this.hsiintersect);
		hsil1dist = new JMenuItem("HSI-L1Distance");
		hsil1dist.addActionListener(this);
		this.menuAlgorithms.add(this.hsil1dist);
		segintersect = new JMenuItem("Intersection-Segmentation");
		segintersect.addActionListener(this);
		this.menuAlgorithms.add(this.segintersect);
		segl1dist = new JMenuItem("L1Distance-Segmentation");
		segl1dist.addActionListener(this);
		this.menuAlgorithms.add(this.segl1dist);
		hsisegintersect = new JMenuItem("HSI-Intersection-Segmentation");
		hsisegintersect.addActionListener(this);
		this.menuAlgorithms.add(this.hsisegintersect);
		hsisegl1dist = new JMenuItem("HSI-L1Distance-Segmentation");
		hsisegl1dist.addActionListener(this);
		this.menuAlgorithms.add(this.hsisegl1dist);
		hsieucdist = new JMenuItem("HSI-Euclidean-Distance");
		hsieucdist.addActionListener(this);
		this.menuAlgorithms.add(this.hsieucdist);
		chisquaresemi = new JMenuItem("HSI-Chi-Square-Semi-Pseudo-Distance");
		chisquaresemi.addActionListener(this);
		this.menuAlgorithms.add(this.chisquaresemi);
		nra_algo = new JMenuItem("NRA-Algorithm");
		nra_algo.addActionListener(this);
		this.menuAlgorithms.add(this.nra_algo);

		histogram = new JMenu("Histogram");
		this.histogram.setMnemonic(KeyEvent.VK_S);
		histGray = new JMenuItem("GrayScale");
		histGray.addActionListener(this);
		this.histogram.add(this.histGray);
		histRGB = new JMenuItem("RGB");
		histRGB.addActionListener(this);
		this.histogram.add(this.histRGB);
		histHSI = new JMenuItem("HSI");
		histHSI.addActionListener(this);
		this.histogram.add(this.histHSI);
		histseggray = new JMenuItem("Segmentation-Gray");
		histseggray.addActionListener(this);
		this.histogram.add(this.histseggray);
		histsegrgb = new JMenuItem("Segmentation-RGB");
		histsegrgb.addActionListener(this);
		this.histogram.add(this.histsegrgb);
		histseghsi = new JMenuItem("Segmentation-HSI");
		histseghsi.addActionListener(this);
		this.histogram.add(this.histseghsi);

		menuBar.add(this.menuFile);
		menuBar.add(this.histogram);
		menuBar.add(this.menuAlgorithms);

		// create widgets
		splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		leftSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		leftCmdPanel = new JPanel();
		leftCmdPanel.setLayout(new BoxLayout(leftCmdPanel, BoxLayout.X_AXIS));

		btnPreview = new JButton("Preview");
		btnPreview.addActionListener(this);
		btnPreview.setToolTipText("Displays a full-sized preview of the currently selected image");
		btnPreview.setMnemonic(KeyEvent.VK_P);

		segmentButton = new JButton("Segmentation");
		segmentButton.addActionListener(this);
		segmentButton.setMnemonic(KeyEvent.VK_P);

		leftCmdPanel.add(btnPreview);
		leftCmdPanel.add(segmentButton);


		// list for displaying the test data imgList
		listFiles = new JList(images);
		listFiles.setCellRenderer(new MRImageCellRenderer());
		listFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.leftScrollPanel = new JScrollPane(listFiles);
		this.leftSplitter.add(this.leftCmdPanel);
		this.leftSplitter.add(this.leftScrollPanel);

		// list for displaying the sorted list of imgList
		this.listSorted = new JList();
		listSorted.setCellRenderer(new MRImageCellRendererSorted());
		listSorted.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		this.rightPanel = new JScrollPane(listSorted);
		rightPanel.setBackground(Color.BLACK);
		rightPanel.setForeground(Color.WHITE);
		rightPanel.setPreferredSize(new Dimension(500, 200));

		splitter.add(this.leftSplitter);
		splitter.add(this.rightPanel);

		// finally add everything to the main frame
		setJMenuBar(menuBar);
		add(splitter);

		this.pack();
		// just in case the frame should start maximized
		//this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * Refreshes the GUI after new test data has been loaded
	 */
	private void refreshGUI() {
		//loadTestData() um Images im ausgewählter Ordner zu laden
		images = loadTestData();
		listFiles = new JList(images);
		listFiles.setCellRenderer(new MRImageCellRenderer());
		listFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Aktualisierung des ScrollPanels mit den neuen Images
		this.leftScrollPanel.getViewport().setView(listFiles);
		//TODO: Refresh der GUI kommt hier, werfen Sie einen Blick auf
		// createGUI() für die interessanten Widgets
	}

	/**
	 * Action handling
	 *
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if (src instanceof JButton) {
			JButton btn = (JButton) src;
			if (btn.getText() == "Preview") {
				// display a full-sized preview
				MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
				if (currentImg != null) {
					JDialog d = new JDialog(this, currentImg.toString());
					JLabel p = new JLabel();
					p.setIcon(new ImageIcon(currentImg.getImage()));
					d.add(p);
					d.setSize(currentImg.getWidth(), currentImg.getHeight());
					d.setResizable(false);
					d.setVisible(true);
				}
			} else if (btn.getText() == "Segmentation") {
				seg = true;
				chooseSegmentationStep();
			} else if (btn.getText() == "OK") {

				String step = tfield.getText();
				int sstep = Integer.parseInt(step);
				if (Integer.bitCount(sstep) == 1) {
					frame.dispose();
					segstep = sstep;

					if (seg == true) {
						createSegmentationView();
					}

					else if(simi == true) {
						similaritySeg();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Segmentationstep must be a value of power of 2 ", "Error", JOptionPane.ERROR_MESSAGE);
					tfield.setText("");
				}

			}  else if (btn.getText() == "Confirm") {
				MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
				String num = tfield1.getText();
				int snumber = Integer.parseInt(num);
				frame1.dispose();
				number = snumber;

				if (eucl == true) {
					this.listSorted.setListData(this.hsieucliddist.applySimilarity(currentImg, images, number, similarity));
					eucl = false;
				}
				else if (nra == true) {
					this.hsieucliddist.applySimilarity(currentImg, images, number, similarity);
					NRA_Algorithm_Sort [] euclidean = hsieucliddist.getNRA_Values();
					this.chisquare.apply(currentImg, images, number);
					NRA_Algorithm_Sort [] chisq = chisquare.getNRA_Values();
					this.listSorted = new JList();
					//listSorted.setCellRenderer(new NRACellRenderer());
					listSorted.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					this.listSorted.setListData(this.nra_algorithm.calculate(euclidean, chisq, number));
					this.rightPanel.getViewport().setView(listSorted);
					nra = false;
				}
			}
		}
		// menu actions
		else if (src instanceof JMenuItem) {
			JMenuItem m = (JMenuItem) src;
			this.listSorted = new JList();
			listSorted.setCellRenderer(new MRImageCellRendererSorted());
			listSorted.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.rightPanel.getViewport().setView(listSorted);
			if (m.getText() == "Exit") {
				System.exit(0);
			} else if (m.getText() == "Intersection") {
				MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
				this.listSorted.setListData(this.intersection.apply(currentImg, images, segstep));
				//Imagepfad ändern und Images laden
			} else if (m.getText() == "Load Testdata") {
				this.refreshGUI();

			} else if (m.getText() == "L1-Distance") {
				MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
				this.listSorted.setListData(this.l1distance.apply(currentImg, images, segstep));
			} else if (m.getText() == "GrayScale") {
				gray = true;
				displayHistogram();
			} else if (m.getText() == "RGB") {
				rgb = true;
				displayHistogram();
			} else if (m.getText() == "HSI") {
				hsi = true;
				displayHistogram();
				//Darstellung der 4 RGB Histogramme des Segmentierten Images
			} else if (m.getText() == "Segmentation-RGB") {
				rgb = true;
				checkImage();
			} else if (m.getText() == "Intersection-Segmentation") {
				simi = true;
				intersec = true;
				chooseSegmentationStep();

			} else if (m.getText() == "L1Distance-Segmentation") {
				simi = true;
				l1dist = true;
				chooseSegmentationStep();

			} else if (m.getText() == "HSI-Intersection-Segmentation") {
				simi = true;
				hsiintersec = true;
				check();

			} else if (m.getText() == "HSI-L1Distance-Segmentation") {
				simi = true;
				hsidist = true;
				check();

			} else if (m.getText() == "HSI-Intersection") {
				MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
				if (currentImg.getImage().getType() == currentImg.getImage().TYPE_BYTE_GRAY) {
					JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					this.listSorted.setListData(this.hsiintersection.apply(currentImg, images, segstep));
				}

			} else if (m.getText() == "HSI-L1Distance") {
				MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
				if (currentImg.getImage().getType() == currentImg.getImage().TYPE_BYTE_GRAY) {
					JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					this.listSorted.setListData(this.hsil1distance.apply(currentImg, images, segstep));
				}

			} else if (m.getText() == "Segmentation-Gray") {
				gray = true;
				checkImage();
			} else if (m.getText() == "Segmentation-HSI") {
				hsi = true;
				checkImage();
			} else if (m.getText() == "HSI-Euclidean-Distance") {
				MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
				if (currentImg.getImage().getType() == currentImg.getImage().TYPE_BYTE_GRAY) {
					JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					eucl = true;
					chooseDominantColorsOrK();
				}
			} else if (m.getText() == "HSI-Chi-Square-Semi-Pseudo-Distance") {
				MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
				if (currentImg.getImage().getType() == currentImg.getImage().TYPE_BYTE_GRAY) {
					JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					this.listSorted.setListData(this.chisquare.apply(currentImg, images, similarity));
				}
			} else if (m.getText() == "NRA-Algorithm") {
				MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
				if (currentImg.getImage().getType() == currentImg.getImage().TYPE_BYTE_GRAY) {
					JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					nra = true;
					chooseDominantColorsOrK();
				}
			}
		}
	}

	private void displayHistogram() {
		MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();

		if (currentImg != null) {
			JDialog d = new JDialog(this, "Histogram: " + currentImg.toString());
			if (currentImg.getImage().getType() == currentImg.getImage().TYPE_BYTE_GRAY) {
				if (rgb == true) {
					JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
					rgb = false;
				}
				else if (hsi == true) {
					JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
					hsi = false;
				}
				else {
					MRHistogramLabel h = new MRHistogramLabel(currentImg.getHistogramGray(imagePath1 + "/" + currentImg.toString()));
					d.add(h);
					d.setSize(256, 480);
					d.setResizable(false);
					d.setVisible(true);
				}
			}
			else if (currentImg.getImage().getType() == currentImg.getImage().TYPE_3BYTE_BGR) {
				if (rgb == true) {
					MRHistogramLabel h = new MRHistogramLabel(currentImg.getHistogramRGB(imagePath1 + "/" + currentImg.toString()));
					d.add(h);
					d.setSize(512, 480);
					d.setResizable(false);
					d.setVisible(true);
					rgb = false;
				}
				else if (hsi == true) {
					String name = currentImg.toString();
					currentImg.generateHistogramHSI(name);
					MRHistogramLabel h = new MRHistogramLabel(currentImg.getHistogramHSI(name));
					d.add(h);
					d.setSize(162, 480);
					d.setResizable(false);
					d.setVisible(true);
					hsi = false;
				}
				else if (gray == true) {
					JOptionPane.showMessageDialog(null, "no GrayScale Image ", "Error", JOptionPane.ERROR_MESSAGE);
					gray = false;
				}
			}
		}
	}

	private void check() {
		MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
		if (currentImg != null) {
			if (currentImg.getImage().getType() == currentImg.getImage().TYPE_BYTE_GRAY) {
				JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
				hsiintersec = false;
				hsidist = false;
			}
			else {
				chooseSegmentationStep();
			}
		}
	}

	private void displayHistogramSegment(Vector<BufferedImage> segment) {
		MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
		if (currentImg != null) {
			int segstep = 4;
			String name = currentImg.toString();
			Vector hist = new Vector();
			JLabel help = new JLabel();
			JDialog d = new JDialog(this, "Histogram: " + currentImg.toString());
			float [] hseg = null;
			float [][][] h1seg = null;
			int x = 0;
			int y = 0;
			int z = 0;

			for (int i = 0; i < segment.size(); i++) {
				MRImage seg = new MRImage (currentImg.filePath,segment.get(i));
				if (rgb == true) {
					seg.generateHistogramRGB(segstep + "Seg" + i + "RGB" + name);
					float [][][] hist1seg = seg.getHistogramRGB(segstep + "Seg" + i + "RGB" + name);
					hist.add(hist1seg);
				}
				else if (hsi == true) {
					seg.generateHistogramHSI(segstep + "Seg" + i + "HSI" + name);
					float [][][] hist1seg = seg.getHistogramHSI(segstep + "Seg" + i + "HSI" + name);
					hist.add(hist1seg);
				}
				else if (gray == true) {
					seg.generateHistogramGray(segstep + "Seg" + i + "Gray" + name);
					float [] hist1seg = seg.getHistogramGray(segstep + "Seg" + i + "Gray" + name);
					hist.add(hist1seg);
				}
			}

			for (int j = 0; j < hist.size(); j++) {
				if (gray == true) {
					hseg = (float []) (hist.get(j));
					MRHistogramLabel h1 = new MRHistogramLabel(hseg);
					h1.setSize(256, 480);
					h1.setLocation(x, y);
					d.add(h1);
					if (z == 1) {
						x = 0;
						y += 480;
						z = 0;
					}
					else {
						x += 256;
						z++;
					}
					d.setSize(512,980);
				}
				else if (rgb == true) {
					h1seg = (float [][][]) (hist.get(j));
					MRHistogramLabel h1 = new MRHistogramLabel(h1seg);
					h1.setSize(512, 480);
					h1.setLocation(x, y);
					d.add(h1);
					if (z == 1) {
						x = 0;
						y += 480;
						z = 0;
					}
					else {
						x += 512;
						z++;
					}
					d.setSize(1024,980);

				}
				else if (hsi == true) {
					h1seg = (float [][][]) (hist.get(j));
					MRHistogramLabel h1 = new MRHistogramLabel(h1seg);
					h1.setSize(162, 480);
					h1.setLocation(x, y);
					d.add(h1);
					if (z == 1) {
						x = 0;
						y += 480;
						z = 0;
					}
					else {
						x += 162;
						z++;
					}
					d.setSize(324,980);
				}

				d.add(help);
				d.setResizable(false);
				d.setVisible(true);
			}
			rgb = false;
			hsi = false;
			gray = false;
		}
	}


	private void checkImage() {
		Vector<BufferedImage> segment = new Vector<BufferedImage>();
		int segstep = 4;
		MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
		if (currentImg != null) {
			if (currentImg.getImage().getType() == currentImg.getImage().TYPE_BYTE_GRAY){
				if (rgb == true) {
					JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
					rgb = false;
				}
				else if (hsi == true) {
					JOptionPane.showMessageDialog(null, "no RGB Image ", "Error", JOptionPane.ERROR_MESSAGE);
					hsi = false;
				}
				else {
					segment = currentImg.generateRaster(segstep);
					displayHistogramSegment(segment);

				}
			}
			else if (currentImg.getImage().getType() == currentImg.getImage().TYPE_3BYTE_BGR) {
				if (gray == true) {
					JOptionPane.showMessageDialog(null, "no GrayScale Image ", "Error", JOptionPane.ERROR_MESSAGE);
					gray = false;
				}
				else {
					segment = currentImg.generateRaster(segstep);
					displayHistogramSegment(segment);
				}
			}
		}

	}

	private void chooseSegmentationStep() {

		MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
		if (currentImg != null) {

			JButton okButton = new JButton("OK");
			tfield = new JTextField();

			okButton.addActionListener(this);

			frame = new JFrame("Choose Segmentationstep");
			frame.setLayout(new GridLayout(2,1));
			frame.add(tfield);
			frame.add(okButton);
			frame.setSize(250, 100);
			frame.setVisible(true);
		}
	}

	private void createSegmentationView() {
		MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
		if (currentImg != null) {

			double imgsize = 0;
			String bit = Integer.toBinaryString(segstep);
			int len = bit.length();
			Vector<BufferedImage> segment = new Vector<BufferedImage>();
			JDialog d = new JDialog(this, currentImg.toString());
			JLabel help = new JLabel();

			if (len % 2 == 0) {
				imgsize = Math.sqrt(segstep * 2);
			}
			else {
				imgsize = Math.sqrt(segstep);
			}
			int isize = (int)(imgsize);
			segment = currentImg.generateRaster(segstep);

			int x = 0;
			int y = 0;
			int z = 0;

			Vector<JLabel> jLabelDynamic = new Vector<JLabel>();

			for (int i = 0; i < segstep; i++) {
				jLabelDynamic.add(new JLabel("Label" + i));
			}

			for (int i = 0; i < segstep; i++) {

				jLabelDynamic.get(i).setIcon(new ImageIcon(segment.get(i)));
				if (len % 2 == 0) {
					jLabelDynamic.get(i).setSize(currentImg.getWidth()/isize, currentImg.getHeight()/ (isize/2));
				}
				else {
					jLabelDynamic.get(i).setSize(currentImg.getWidth()/isize, currentImg.getHeight()/isize);
				}

				d.add(jLabelDynamic.get(i));
				jLabelDynamic.get(i).setLocation(x, y);

				if (len % 2 == 0) {
					if (z == isize - 1) {
						x = 0;
						y += currentImg.getHeight()/ (isize/2) + 10;
						z = 0;
					}
					else {
						x += currentImg.getWidth()/isize + 10;
						z++;
					}
				}
				else {
					if (z == isize - 1) {
						x = 0;
						y += currentImg.getHeight()/ isize + 10;
						z = 0;
					}
					else {
						x += currentImg.getWidth()/isize + 10;
						z++;
					}
				}
			}
			d.add(help);
			d.setSize(currentImg.getWidth()+ (isize-1) * 10, currentImg.getHeight()+ (isize +1)*10);
			d.setResizable(false);
			d.setVisible(true);
			segstep = 0;
			seg = false;
		}
	}

	private void similaritySeg() {
		MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
		if (intersec == true) {
			this.listSorted.setListData(this.segintersection.apply(currentImg, images, segstep));
			intersec = false;
		}
		else if (l1dist == true) {
			this.listSorted.setListData(this.segl1distance.apply(currentImg, images, segstep));
			l1dist = false;
		}
		else if (hsiintersec == true) {
			this.listSorted.setListData(this.hsisegintersection.apply(currentImg, images, segstep));
			hsiintersec = false;
		}
		else if (hsidist == true) {
			this.listSorted.setListData(this.hsisegl1distance.apply(currentImg, images, segstep));
			hsidist = false;
		}
		segstep = 0;
		simi = false;
	}

	private void chooseDominantColorsOrK() {
		MRImage currentImg = (MRImage) this.listFiles.getSelectedValue();
		if (currentImg != null) {

			JButton okButton = new JButton("Confirm");
			tfield1 = new JTextField();

			okButton.addActionListener(this);

			if (eucl == true) {
				frame1 = new JFrame("Choose Number of dominant Colors");
			}

			else if (nra == true) {
				frame1 = new JFrame("Choose K");
			}
			frame1.setLayout(new GridLayout(2,1));
			frame1.add(tfield1);
			frame1.add(okButton);
			frame1.setSize(300, 150);
			frame1.setVisible(true);
		}
	}

	//Filechooser öffnen und Images laden
	private Vector<MRImage> loadTestData() {
		//TODO:
		// Setzen Sie hier eine Auswahlmöglichkeit für das Bilderverzeichnis
		// Passen Sie diesen Pfad so an, dass er auf ein existierendes Verzeichnis
		// verweist, welches Graustufenbilder enthält. Ansonsten startet die
		// Anwendung nicht. Der Ladecode weiter unten dient zur Demonstration,
		// damit die Anwendung etwas anzeigt und man die GUI erkunden kann.
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose Folder");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setVisible(true);
		int returnVal = fileChooser.showDialog(this, "Select Path");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			imagePath1 = file.getAbsolutePath();
			System.out.println("Selected Path: " + file.getAbsolutePath());
		} else {
			System.out.println("Chanceled");
		}
		// try to load imgList from this directory
		File f = new File(imagePath1);

		// load all imgList and create their histograms if the directory exists
		if (f.exists()) {
			//splash.setMessage("Loading imgList...");
			Vector<MRImage> imgList = new Vector<MRImage>();
			File[] files = f.listFiles();

			for (int i = 0; i < files.length; i++) {
				if (files[i].toString().toLowerCase().endsWith("jpg")) {
					BufferedImage img = null;
					try {
						img = ImageIO.read(files[i]);
					} catch (IOException e) {
						System.err.println("Could not read file: " + files[i]);
						e.printStackTrace();
					}
					if (img != null) {
						if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
							MRImage mrImage = new MRImage(files[i].toString(),
							                              img);
							mrImage.generateHistogramGray(files[i].toString());
							imgList.add(mrImage);
						}
						else if (img.getType() == BufferedImage.TYPE_3BYTE_BGR) {
							MRImage mrImage = new MRImage(files[i].toString(),
							                              img);
							mrImage.generateHistogramRGB(files[i].toString());
							imgList.add(mrImage);
						}
						else {
							System.out.println("Failed for " + files[i] + "; image is no 8-Bit grayscale image");
						}
					}
				}
			}

			System.out.println("Total number of loaded images: " + imgList.size());

			return imgList;
		} else {

			JOptionPane.showMessageDialog(null, "Could not open sample image path:\n" + f.getAbsolutePath() + "\n\nThe application will exit.", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
}