package com.and1.gui;

import com.and1.*;
import com.and1.gui.Renderer.MRImageCellRenderer;
import com.and1.gui.Renderer.MRImageCellRendererSorted;
import com.and1.img.MRImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MainFrame extends JFrame implements ActionListener
{
	private static final Logger logger = LogManager.getLogger(MainFrame.class);

	private JScrollPane leftScrollPanel;
	private JScrollPane rightPanel;
	private JList jListFiles, jListFilesSorted;
	private Vector<MRImage> imagesList;
	private final Intersection intersection = new Intersection();
	private final L1Distance l1distance = new L1Distance();
	private final Seg_Intersection segmentationIntersection = new Seg_Intersection();
	private final Seg_L1Distance segmentationL1Distance = new Seg_L1Distance();
	private final HSI_Intersection hsiIntersection = new HSI_Intersection();
	private final HSI_L1Distance hsiL1Distance = new HSI_L1Distance();
	private final HSISeg_L1Distance hsiSegmentationL1Distance = new HSISeg_L1Distance();
	private final HSI_Euclidean_Distance hsiEuclidDistance = new HSI_Euclidean_Distance();
	private final Chi_Square_Semi_Pseudo_Distance chiSquare = new Chi_Square_Semi_Pseudo_Distance();
	private final NRA_Algorithm nraAlgorithm = new NRA_Algorithm();
	private JFrame frame, frame1;
	private int segmentationStep;
	private JTextField jTextField, jTextField1;
	private Boolean seg, simi, l1dist, intersec, hsidist, hsiintersec, rgb, hsi, gray, eucl, nra;
	private JLabel statusBarLabel;

	public MainFrame(MRSplashScreen splash, Thread splashThread) throws HeadlessException
	{
		super("Multimedia image Recognition");

		File pathToImage = openDirectoryChooser();

		imagesList = loadTestData(pathToImage);

		if(imagesList != null)
		{
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
			statusBarLabel.setText("Total number of loaded imagesList: " + imagesList.size());

			logger.info("Total number of loaded imagesList: " + imagesList.size());

			splash.dispose();
			splashThread.interrupt();
		} else
		{
			JOptionPane.showMessageDialog(null, "Could not load files", "Error", JOptionPane.ERROR_MESSAGE);
			splash.dispose();
			splashThread.interrupt();
			System.exit(1);
		}

	}

	private void createGUI()
	{
		setLayout(new BorderLayout());

		JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JSplitPane leftSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		JPanel leftCmdPanel = new JPanel();
		leftCmdPanel.setLayout(new BoxLayout(leftCmdPanel, BoxLayout.X_AXIS));

		JButton btnPreview = new JButton("Preview");
		btnPreview.addActionListener(this);
		btnPreview.setToolTipText("Displays a full-sized preview of the currently selected image");
		btnPreview.setMnemonic(KeyEvent.VK_P);

		JButton segmentButton = new JButton("Segmentation");
		segmentButton.addActionListener(this);
		segmentButton.setMnemonic(KeyEvent.VK_P);

		leftCmdPanel.add(btnPreview);
		leftCmdPanel.add(segmentButton);

		// list for displaying the test data imgList
		jListFiles = new JList(imagesList);
		jListFiles.setCellRenderer(new MRImageCellRenderer());
		jListFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		leftScrollPanel = new JScrollPane(jListFiles);
		leftSplitter.add(leftCmdPanel);
		leftSplitter.add(leftScrollPanel);

		// list for displaying the sorted list of imgList
		jListFilesSorted = new JList();
		jListFilesSorted.setCellRenderer(new MRImageCellRendererSorted());
		jListFilesSorted.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		rightPanel = new JScrollPane(jListFilesSorted);
		rightPanel.setBackground(Color.BLACK);
		rightPanel.setForeground(Color.WHITE);
		rightPanel.setPreferredSize(new Dimension(1700, 800));

		splitter.add(leftSplitter);
		splitter.add(rightPanel);

		// finally add everything to the main frame
		setJMenuBar(createJMenuBar());
		add(splitter);

		// create the status bar panel and shove it down the bottom of the frame
		JPanel statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusBar, BorderLayout.SOUTH);
//		statusBar.setPreferredSize(new Dimension(frame.getWidth(), 16));
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		statusBarLabel = new JLabel("status");
		statusBarLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusBar.add(statusBarLabel);
		add(statusBar, BorderLayout.SOUTH);

		// just in case the frame should start maximized
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}

	private JMenuBar createJMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();

		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		JMenuItem cmdFileChoose = new JMenuItem("Load Testdata");
		cmdFileChoose.addActionListener(this);

		menuFile.add(cmdFileChoose);

		JMenuItem cmdExit = new JMenuItem("Exit");
		cmdExit.addActionListener(this);
		menuFile.add(cmdExit);

		JMenu menuAlgorithms = new JMenu("Similarity Algorithms");
		menuAlgorithms.setMnemonic(KeyEvent.VK_S);
		JMenuItem intersect = new JMenuItem("com.and1.Intersection");
		intersect.addActionListener(this);
		menuAlgorithms.add(intersect);
		JMenuItem l1Distance = new JMenuItem("L1-Distance");
		l1Distance.addActionListener(this);
		menuAlgorithms.add(l1Distance);
		JMenuItem hsiintersect = new JMenuItem("HSI-com.and1.Intersection");
		hsiintersect.addActionListener(this);
		menuAlgorithms.add(hsiintersect);
		JMenuItem hsil1dist = new JMenuItem("HSI-com.and1.L1Distance");
		hsil1dist.addActionListener(this);
		menuAlgorithms.add(hsil1dist);
		JMenuItem segintersect = new JMenuItem("com.and1.Intersection-Segmentation");
		segintersect.addActionListener(this);
		menuAlgorithms.add(segintersect);
		JMenuItem segl1dist = new JMenuItem("com.and1.L1Distance-Segmentation");
		segl1dist.addActionListener(this);
		menuAlgorithms.add(segl1dist);
		JMenuItem hsisegintersect = new JMenuItem("HSI-com.and1.Intersection-Segmentation");
		hsisegintersect.addActionListener(this);
		menuAlgorithms.add(hsisegintersect);
		JMenuItem hsisegl1dist = new JMenuItem("HSI-com.and1.L1Distance-Segmentation");
		hsisegl1dist.addActionListener(this);
		menuAlgorithms.add(hsisegl1dist);
		JMenuItem hsieucdist = new JMenuItem("HSI-Euclidean-Distance");
		hsieucdist.addActionListener(this);
		menuAlgorithms.add(hsieucdist);
		JMenuItem chisquaresemi = new JMenuItem("HSI-Chi-Square-Semi-Pseudo-Distance");
		chisquaresemi.addActionListener(this);
		menuAlgorithms.add(chisquaresemi);
		JMenuItem nra_algo = new JMenuItem("NRA-Algorithm");
		nra_algo.addActionListener(this);
		menuAlgorithms.add(nra_algo);

		JMenu histogram = new JMenu("com.and1.img.histogram.Histogram");
		histogram.setMnemonic(KeyEvent.VK_S);
		JMenuItem histGray = new JMenuItem("GrayScale");
		histGray.addActionListener(this);
		histogram.add(histGray);
		JMenuItem histRGB = new JMenuItem("RGB");
		histRGB.addActionListener(this);
		histogram.add(histRGB);
		JMenuItem histHSI = new JMenuItem("HSI");
		histHSI.addActionListener(this);
		histogram.add(histHSI);
		JMenuItem histseggray = new JMenuItem("Segmentation-Gray");
		histseggray.addActionListener(this);
		histogram.add(histseggray);
		JMenuItem histsegrgb = new JMenuItem("Segmentation-RGB");
		histsegrgb.addActionListener(this);
		histogram.add(histsegrgb);
		JMenuItem histseghsi = new JMenuItem("Segmentation-HSI");
		histseghsi.addActionListener(this);
		histogram.add(histseghsi);

		menuBar.add(menuFile);
		menuBar.add(histogram);
		menuBar.add(menuAlgorithms);
		return menuBar;
	}

	/**
	 * Refreshes the GUI after new test data has been loaded
	 */
	private void refreshGUI()
	{
		imagesList = loadTestData(openDirectoryChooser());
		jListFiles = new JList(imagesList);
		jListFiles.setCellRenderer(new MRImageCellRenderer());
		jListFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//Aktualisierung des ScrollPanels mit den neuen Images
		leftScrollPanel.getViewport().setView(jListFiles);
		//TODO: Refresh der GUI kommt hier, werfen Sie einen Blick auf
		// createGUI() f�r die interessanten Widgets
		System.out.println("refreshGUI ---------");
	}

	/**
	 * Action handling
	 *
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		Object src = arg0.getSource();
		int similarity = 1;
		if(src instanceof JButton)
		{
			JButton btn = (JButton) src;
			if(btn.getText().equals("Preview"))
			{
				// display a full-sized preview
				MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
				if(currentImg != null)
				{
					JDialog d = new JDialog(this, currentImg.toString());
					JLabel p = new JLabel();
					p.setIcon(new ImageIcon(currentImg.getImage()));
					d.add(p);
					d.setSize(currentImg.getWidth(), currentImg.getHeight());
					d.setResizable(false);
					d.setVisible(true);
				}
			} else if(btn.getText().equals("Segmentation"))
			{
				seg = true;
				chooseSegmentationStep();
			} else if(btn.getText().equals("OK"))
			{

				String step = jTextField.getText();
				int sstep = Integer.parseInt(step);
				if(Integer.bitCount(sstep) == 1)
				{
					frame.dispose();
					segmentationStep = sstep;

					if(seg)
					{
						createSegmentationView();
					} else if(simi)
					{
						similaritySeg();
					}
				} else
				{
					JOptionPane.showMessageDialog(null, "Segmentationstep must be a value of power of 2 ", "Error",
					                              JOptionPane.ERROR_MESSAGE);
					jTextField.setText("");
				}

			} else if(btn.getText().equals("Confirm"))
			{

				MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
				String num = jTextField1.getText();
				int snumber = Integer.parseInt(num);
				frame1.dispose();

				if(eucl)
				{
					jListFilesSorted.setListData(
							hsiEuclidDistance.applySimilarity(currentImg, imagesList, snumber, similarity));
					eucl = false;
				} else if(nra)
				{
					hsiEuclidDistance.applySimilarity(currentImg, imagesList, snumber, similarity);
					NRA_Algorithm_Sort[] euclidean = hsiEuclidDistance.getNRA_Values();
					chiSquare.apply(currentImg, imagesList, snumber);
					NRA_Algorithm_Sort[] chisq = chiSquare.getNRA_Values();
					jListFilesSorted = new JList();
					jListFilesSorted.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					jListFilesSorted.setListData(nraAlgorithm.calculate(euclidean, chisq, snumber));
					rightPanel.getViewport().setView(jListFilesSorted);
					nra = false;
				}
			}
		}
		// menu actions
		else if(src instanceof JMenuItem)
		{
			JMenuItem m = (JMenuItem) src;
			jListFilesSorted = new JList();
			jListFilesSorted.setCellRenderer(new MRImageCellRendererSorted());
			jListFilesSorted.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			rightPanel.getViewport().setView(jListFilesSorted);
			if(m.getText().equals("Exit"))
			{

				System.exit(0);
			} else if(m.getText().equals("com.and1.Intersection"))
			{

				MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
				jListFilesSorted.setListData(intersection.apply(currentImg, imagesList, segmentationStep));
				//Imagepfad �ndern und Images laden
			} else if(m.getText().equals("Load Testdata"))
			{

				refreshGUI();

			} else if(m.getText().equals("L1-Distance"))
			{

				MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
				jListFilesSorted.setListData(l1distance.apply(currentImg, imagesList, segmentationStep));
			} else if(m.getText().equals("GrayScale"))
			{

				gray = true;
				displayHistogram();
			} else if(m.getText().equals("RGB"))
			{

				rgb = true;
				displayHistogram();
			} else if(m.getText().equals("HSI"))
			{

				hsi = true;
				displayHistogram();
				//Darstellung der 4 RGB Histogramme des Segmentierten Images
			} else if(m.getText().equals("Segmentation-RGB"))
			{

				rgb = true;
				checkImage();
			} else if(m.getText().equals("com.and1.Intersection-Segmentation"))
			{

				simi = true;
				intersec = true;
				chooseSegmentationStep();

			} else if(m.getText().equals("com.and1.L1Distance-Segmentation"))
			{

				simi = true;
				l1dist = true;
				chooseSegmentationStep();

			} else if(m.getText().equals("HSI-com.and1.Intersection-Segmentation") || m.getText().equals("HSI-com.and1.L1Distance-Segmentation"))
			{

				simi = true;
				hsiintersec = true;
				check((MRImage) jListFiles.getSelectedValue());

			} else if(m.getText().equals("HSI-com.and1.Intersection"))
			{

				MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					jListFilesSorted.setListData(hsiIntersection.apply(currentImg, imagesList, segmentationStep));
				}

			} else if(m.getText().equals("HSI-com.and1.L1Distance"))
			{

				MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					jListFilesSorted.setListData(hsiL1Distance.apply(currentImg, imagesList, segmentationStep));
				}

			} else if(m.getText().equals("Segmentation-Gray"))
			{

				gray = true;
				checkImage();
			} else if(m.getText().equals("Segmentation-HSI"))
			{

				hsi = true;
				checkImage();
			} else if(m.getText().equals("HSI-Euclidean-Distance"))
			{
				MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					eucl = true;
					chooseDominantColorsOrK();
				}
			} else if(m.getText().equals("HSI-Chi-Square-Semi-Pseudo-Distance"))
			{

				MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					jListFilesSorted.setListData(chiSquare.apply(currentImg, imagesList, similarity));
				}
			} else if(m.getText().equals("NRA-Algorithm"))
			{

				MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					nra = true;
					chooseDominantColorsOrK();
				}
			}
		}
	}

	private void displayHistogram()
	{
		MRImage currentImg = (MRImage) jListFiles.getSelectedValue();

		JDialog d = new JDialog(this, "com.and1.img.histogram.Histogram: " + currentImg.toString());
		String imagePath1 = "";

		if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			if(rgb || hsi)
			{
				JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				rgb = false;
			} else
			{
				MRHistogramLabel h =
						new MRHistogramLabel(currentImg.getHistogramGray(imagePath1 + "/" + currentImg.toString()));
				d.add(h);
				d.setSize(256, 480);
				d.setResizable(false);
				d.setVisible(true);
			}
		} else if(currentImg.getImage().getType() == BufferedImage.TYPE_3BYTE_BGR)
		{
			if(rgb)
			{
				MRHistogramLabel h =
						new MRHistogramLabel(currentImg.getHistogramRGB(imagePath1 + "/" + currentImg.toString()));
				d.add(h);
				d.setSize(512, 480);
				d.setResizable(false);
				d.setVisible(true);
				rgb = false;
			} else if(hsi)
			{
				String name = currentImg.toString();
				currentImg.generateHistogramHSI(name);
				MRHistogramLabel h = new MRHistogramLabel(currentImg.getHistogramHSI(name));
				d.add(h);
				d.setSize(162, 480);
				d.setResizable(false);
				d.setVisible(true);
				hsi = false;
			} else if(gray)
			{
				JOptionPane.showMessageDialog(null, "no GrayScale image ", "Error", JOptionPane.ERROR_MESSAGE);
				gray = false;
			}
		}
	}

	private void check(MRImage image)
	{
		if(image.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
			hsiintersec = false;
			hsidist = false;
		} else
		{
			chooseSegmentationStep();
		}

	}

	private void displayHistogramSegment(Vector<BufferedImage> segment)
	{
		MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
		if(currentImg != null)
		{
			int segstep = 4;
			String name = currentImg.toString();
			Vector hist = new Vector();
			JLabel help = new JLabel();
			JDialog d = new JDialog(this, "com.and1.img.histogram.Histogram: " + currentImg.toString());
			float[] hseg;
			float[][][] h1seg;
			int x = 0;
			int y = 0;
			int z = 0;

			for(int i = 0; i < segment.size(); i++)
			{
				MRImage segmentImage = new MRImage(currentImg.getFilePath(), segment.get(i));
				if(rgb)
				{
					segmentImage.generateHistogramRGB(segstep + "Seg" + i + "RGB" + name);
					float[][][] hist1seg = segmentImage.getHistogramRGB(segstep + "Seg" + i + "RGB" + name);
					hist.add(hist1seg);
				} else if(hsi)
				{
					segmentImage.generateHistogramHSI(segstep + "Seg" + i + "HSI" + name);
					float[][][] hist1seg = segmentImage.getHistogramHSI(segstep + "Seg" + i + "HSI" + name);
					hist.add(hist1seg);
				} else if(gray)
				{
					segmentImage.generateHistogramGray(segstep + "Seg" + i + "Gray" + name);
					float[] hist1seg = segmentImage.getHistogramGray(segstep + "Seg" + i + "Gray" + name);
					hist.add(hist1seg);
				}
			}

			for(Object aHist : hist)
			{
				if(gray)
				{
					hseg = (float[]) aHist;
					MRHistogramLabel h1 = new MRHistogramLabel(hseg);
					h1.setSize(256, 480);
					h1.setLocation(x, y);
					d.add(h1);
					if(z == 1)
					{
						x = 0;
						y += 480;
						z = 0;
					} else
					{
						x += 256;
						z++;
					}
					d.setSize(512, 980);
				} else if(rgb)
				{
					h1seg = (float[][][]) aHist;
					MRHistogramLabel h1 = new MRHistogramLabel(h1seg);
					h1.setSize(512, 480);
					h1.setLocation(x, y);
					d.add(h1);
					if(z == 1)
					{
						x = 0;
						y += 480;
						z = 0;
					} else
					{
						x += 512;
						z++;
					}
					d.setSize(1024, 980);

				} else if(hsi)
				{
					h1seg = (float[][][]) aHist;
					MRHistogramLabel h1 = new MRHistogramLabel(h1seg);
					h1.setSize(162, 480);
					h1.setLocation(x, y);
					d.add(h1);
					if(z == 1)
					{
						x = 0;
						y += 480;
						z = 0;
					} else
					{
						x += 162;
						z++;
					}
					d.setSize(324, 980);
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

	private void checkImage()
	{
		Vector<BufferedImage> segment;
		int segstep = 4;
		MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
		if(currentImg != null)
		{
			if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
			{
				if(rgb)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
					rgb = false;
				} else if(hsi)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
					hsi = false;
				} else
				{
					segment = currentImg.generateRasterInGivenSteps(segstep);
					displayHistogramSegment(segment);

				}
			} else if(currentImg.getImage().getType() == BufferedImage.TYPE_3BYTE_BGR)
			{
				if(gray)
				{
					JOptionPane.showMessageDialog(null, "no GrayScale image ", "Error", JOptionPane.ERROR_MESSAGE);
					gray = false;
				} else
				{
					segment = currentImg.generateRasterInGivenSteps(segstep);
					displayHistogramSegment(segment);
				}
			}
		}

	}

	private void chooseSegmentationStep()
	{

		MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
		if(currentImg != null)
		{

			JButton okButton = new JButton("OK");
			jTextField = new JTextField();

			okButton.addActionListener(this);

			frame = new JFrame("Choose Segmentationstep");
			frame.setLayout(new GridLayout(2, 1));
			frame.add(jTextField);
			frame.add(okButton);
			frame.setSize(250, 100);
			frame.setVisible(true);
		}
	}

	private void createSegmentationView()
	{
		MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
		if(currentImg != null)
		{

			double imageSize;
			String bit = Integer.toBinaryString(segmentationStep);
			int len = bit.length();
			Vector<BufferedImage> segment;
			JDialog d = new JDialog(this, currentImg.toString());
			JLabel help = new JLabel();

			if(len % 2 == 0)
			{
				imageSize = Math.sqrt(segmentationStep * 2);
			} else
			{
				imageSize = Math.sqrt(segmentationStep);
			}
			int isize = (int) (imageSize);
			segment = currentImg.generateRasterInGivenSteps(segmentationStep);

			int x = 0;
			int y = 0;
			int z = 0;

			Vector<JLabel> jLabelDynamic = new Vector<>();

			for(int i = 0; i < segmentationStep; i++)
			{
				jLabelDynamic.add(new JLabel("Label" + i));
			}

			for(int i = 0; i < segmentationStep; i++)
			{

				jLabelDynamic.get(i).setIcon(new ImageIcon(segment.get(i)));
				if(len % 2 == 0)
				{
					jLabelDynamic.get(i).setSize(currentImg.getWidth() / isize, currentImg.getHeight() / (isize / 2));
				} else
				{
					jLabelDynamic.get(i).setSize(currentImg.getWidth() / isize, currentImg.getHeight() / isize);
				}

				d.add(jLabelDynamic.get(i));
				jLabelDynamic.get(i).setLocation(x, y);

				if(len % 2 == 0)
				{
					if(z == isize - 1)
					{
						x = 0;
						y += currentImg.getHeight() / (isize / 2) + 10;
						z = 0;
					} else
					{
						x += currentImg.getWidth() / isize + 10;
						z++;
					}
				} else
				{
					if(z == isize - 1)
					{
						x = 0;
						y += currentImg.getHeight() / isize + 10;
						z = 0;
					} else
					{
						x += currentImg.getWidth() / isize + 10;
						z++;
					}
				}
			}
			d.add(help);
			d.setSize(currentImg.getWidth() + (isize - 1) * 10, currentImg.getHeight() + (isize + 1) * 10);
			d.setResizable(false);
			d.setVisible(true);
			segmentationStep = 0;
			seg = false;
		}
	}

	private void similaritySeg()
	{
		MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
		if(intersec)
		{
			jListFilesSorted.setListData(segmentationIntersection.apply(currentImg, imagesList, segmentationStep));
			intersec = false;
		} else if(l1dist)
		{
			jListFilesSorted.setListData(segmentationL1Distance.apply(currentImg, imagesList, segmentationStep));
			l1dist = false;
		} else if(hsidist || hsiintersec)
		{
			jListFilesSorted.setListData(hsiSegmentationL1Distance.apply(currentImg, imagesList, segmentationStep));
			hsidist = false;
		}
		segmentationStep = 0;
		simi = false;
	}

	private void chooseDominantColorsOrK()
	{
		MRImage currentImg = (MRImage) jListFiles.getSelectedValue();
		if(currentImg != null)
		{

			JButton okButton = new JButton("Confirm");
			jTextField1 = new JTextField();

			okButton.addActionListener(this);

			if(eucl)
			{
				frame1 = new JFrame("Choose Number of dominant Colors");
			} else if(nra)
			{
				frame1 = new JFrame("Choose K");
			}
			frame1.setLayout(new GridLayout(2, 1));
			frame1.add(jTextField1);
			frame1.add(okButton);
			frame1.setSize(300, 150);
			frame1.setVisible(true);
		}
	}

	private Vector<MRImage> loadTestData(File pathToImage)
	{
		Vector<MRImage> imageVector = null;
		// load all imgList and create their histograms if the directory exists
		if(pathToImage != null && pathToImage.exists())
		{
			File[] fileList = pathToImage.listFiles();

			if(fileList != null)
			{
				imageVector = new Vector<>();
				BufferedImage image;

				for(File file : fileList)
				{
					if(file.toString().toLowerCase().endsWith("jpg"))
					{
						image = getBufferedImageFromFile(file);

						if(image != null)
						{
							if(image.getType() == BufferedImage.TYPE_BYTE_GRAY)
							{
								MRImage mrImage = new MRImage(file.getAbsoluteFile(), image);
								mrImage.generateHistogramGray(file.toString());
								imageVector.add(mrImage);
							} else if(image.getType() == BufferedImage.TYPE_3BYTE_BGR)
							{
								MRImage mrImage = new MRImage(file.getAbsoluteFile(), image);
								mrImage.generateHistogramRGB(file.toString());
								imageVector.add(mrImage);
							} else
							{
								System.out.println("Failed for " + file + "; image is no 8-Bit grayscale image");
							}
						}
					}
				}

				System.out.println("Total number of loaded imagesList: " + imageVector.size());

			} else
			{
				JOptionPane.showMessageDialog(null, "No images in path:\n" + pathToImage.getAbsolutePath(), "Error",
				                              JOptionPane.ERROR_MESSAGE);
			}
		}

		return imageVector;
	}

	private BufferedImage getBufferedImageFromFile(File file)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(file);
		} catch(IOException e)
		{
			statusBarLabel.setText("Could not read files!!!");
			System.err.println("Could not read file: " + file);
			e.printStackTrace();
		}
		return img;
	}

	private File openDirectoryChooser()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose Folder");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setVisible(true);

		int returnVal = fileChooser.showDialog(this, "Select Path");

		File file = null;

		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			file = fileChooser.getSelectedFile();

			System.out.println("Selected Path: " + file.getAbsolutePath());
		}

		return file;
	}
}