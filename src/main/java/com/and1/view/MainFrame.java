package com.and1.view;

import com.and1.algorithm.HSI_Euclidean_Distance;
import com.and1.algorithm.NRA_Algorithm;
import com.and1.algorithm.*;
import com.and1.model.img.Image;
import com.and1.sort.NRA_Algorithm_Sort;
import com.and1.view.label.HistogramLabel;
import com.and1.view.label.HistogramLabelGray;
import com.and1.view.label.HistogramLabelHSI;
import com.and1.view.label.HistogramLabelRGB;
import com.and1.view.renderer.ImageCellRenderer;
import com.and1.view.renderer.ImageCellRendererSorted;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class MainFrame extends JFrame implements ActionListener
{
	private static final Logger logger = LogManager.getLogger(MainFrame.class);
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
	private JScrollPane leftScrollPanel;
	private JScrollPane rightPanel;
	private JList jListFiles, jListFilesRanked;
	private Vector<Image> imagesList;
	private int segmentationStep;

	private JFrame frame, frame1;
	private JTextField jTextField, jTextField1;
	private Boolean rgb, hsi, gray, eucl, nra;
	private JLabel statusBarLabel;

	MainFrame(ImageSplashScreen splash, Thread splashThread) throws HeadlessException
	{
		super("Image Recognition");

		File pathToImage = openDirectoryChooser();

		imagesList = loadTestData(pathToImage);

		if(imagesList != null)
		{
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
			logger.info("system exit...");
			System.exit(1);
		}

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

			logger.info("Selected Path: " + file.getAbsolutePath());
		}

		return file;
	}

	private Vector<Image> loadTestData(File pathToImage)
	{
		Vector<Image> imageVector = null;
		// load all jpg and create their histograms if the directory exists
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
								Image mrImage = new Image(file.getAbsoluteFile(), image);

								// TODO create or load histogram if it is necessary
								mrImage.generateHistogramGray(file.toString());

								imageVector.add(mrImage);
							} else if(image.getType() == BufferedImage.TYPE_3BYTE_BGR)
							{
								Image mrImage = new Image(file.getAbsoluteFile(), image);
								mrImage.generateHistogramRGB(file.toString());
								imageVector.add(mrImage);
							} else
							{
								logger.info("Failed for " + file + "; image is no 8-Bit grayscale image");
							}
						}
					}
				}

				logger.info("Total number of loaded imagesList: " + imageVector.size());

			} else
			{
				JOptionPane.showMessageDialog(null, "No images in path:\n" + pathToImage.getAbsolutePath(), "Error",
				                              JOptionPane.ERROR_MESSAGE);
			}
		}

		return imageVector;
	}

	private void createGUI()
	{
		setLayout(new BorderLayout());

		JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JSplitPane leftSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		JPanel leftCmdPanel = new JPanel();
		leftCmdPanel.setLayout(new BoxLayout(leftCmdPanel, BoxLayout.X_AXIS));

		JButton btnPreview = new JButton("Preview");

		ActionListenerFunction function = new ActionListenerFunction();

		btnPreview.addActionListener(e -> function.actionPreview((Image) jListFiles.getSelectedValue(), frame));

		btnPreview.setToolTipText("Displays a full-sized preview of the currently selected image");
		btnPreview.setMnemonic(KeyEvent.VK_P);

		JButton segmentButton = new JButton("Segmentation");
		segmentButton.addActionListener(e -> function.actionSegmentation(frame, (Image) jListFiles.getSelectedValue()));
		segmentButton.setMnemonic(KeyEvent.VK_P);

		leftCmdPanel.add(btnPreview);
		leftCmdPanel.add(segmentButton);

		// list for displaying the test data imgList
		jListFiles = new JList(imagesList);
		jListFiles.setCellRenderer(new ImageCellRenderer());
		jListFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		leftScrollPanel = new JScrollPane(jListFiles);
		leftSplitter.add(leftCmdPanel);
		leftSplitter.add(leftScrollPanel);

		// list for displaying the sorted list of imgList
		jListFilesRanked = new JList();
		jListFilesRanked.setCellRenderer(new ImageCellRendererSorted());
		jListFilesRanked.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		rightPanel = new JScrollPane(jListFilesRanked);
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

	private BufferedImage getBufferedImageFromFile(File file)
	{
		BufferedImage img = null;

		try
		{
			img = ImageIO.read(file);
		} catch(IOException e)
		{
			statusBarLabel.setText("Could not read files!!!");
			logger.error("Could not read file: " + file, e);
		}
		return img;
	}

	private JMenuBar createJMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();

		ActionListenerFunction function = new ActionListenerFunction();

		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		JMenuItem cmdFileChoose = new JMenuItem("Load Testdata");
		cmdFileChoose.addActionListener(e -> refreshGUI());

		menuFile.add(cmdFileChoose);

		JMenuItem cmdExit = new JMenuItem("Exit");
		cmdExit.addActionListener(e -> function.actionExit());
		menuFile.add(cmdExit);

		JMenu menuAlgorithms = new JMenu("Similarity Algorithms");
		menuAlgorithms.setMnemonic(KeyEvent.VK_S);

		JMenuItem intersect = new JMenuItem("Intersection");
		intersect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Image currentImg = (Image) jListFiles.getSelectedValue();

				//TODO check if in original code segmentationStep has value != 0
				// read number and convert it to a power of 2
				logger.info("segmentation step: " + segmentationStep);

				jListFilesRanked.setListData(intersection.apply(currentImg, imagesList, segmentationStep));
			}
		});
		menuAlgorithms.add(intersect);

		JMenuItem l1Distance = new JMenuItem("L1-Distance");
		l1Distance.addActionListener(this);
		menuAlgorithms.add(l1Distance);

		JMenuItem hsiintersect = new JMenuItem("HSI-Intersection");
		hsiintersect.addActionListener(this);
		menuAlgorithms.add(hsiintersect);

		JMenuItem hsil1dist = new JMenuItem("HSI-L1Distance");
		hsil1dist.addActionListener(this);
		menuAlgorithms.add(hsil1dist);

		JMenuItem segintersect = new JMenuItem("Intersection-Segmentation");
		segintersect.addActionListener(this);
		menuAlgorithms.add(segintersect);

		JMenuItem segl1dist = new JMenuItem("L1Distance-Segmentation");
		segl1dist.addActionListener(this);
		menuAlgorithms.add(segl1dist);

		JMenuItem hsisegintersect = new JMenuItem("HSI-Intersection-Segmentation");
		hsisegintersect.addActionListener(this);
		menuAlgorithms.add(hsisegintersect);

		JMenuItem hsisegl1dist = new JMenuItem("HSI-L1Distance-Segmentation");
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

		JMenu histogram = new JMenu("Histogram");
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
		histseggray.addActionListener(e -> checkImage());
		histogram.add(histseggray);
		JMenuItem histsegrgb = new JMenuItem("Segmentation-RGB");
		histsegrgb.addActionListener(e -> checkImage());
		histogram.add(histsegrgb);
		JMenuItem histseghsi = new JMenuItem("Segmentation-HSI");
		histseghsi.addActionListener(e -> checkImage());
		histogram.add(histseghsi);

		menuBar.add(menuFile);
		menuBar.add(histogram);
		menuBar.add(menuAlgorithms);
		return menuBar;
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

		// menu actions
		if(src instanceof JMenuItem)
		{
			JMenuItem m = (JMenuItem) src;
			jListFilesRanked = new JList();
			jListFilesRanked.setCellRenderer(new ImageCellRendererSorted());
			jListFilesRanked.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			rightPanel.getViewport().setView(jListFilesRanked);

			if(m.getText().equals("L1-Distance"))
			{

				Image currentImg = (Image) jListFiles.getSelectedValue();
				jListFilesRanked.setListData(l1distance.apply(currentImg, imagesList, segmentationStep));
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
			} else if(m.getText().equals("Intersection-Segmentation"))
			{
				segmentationStep = chooseInputWindow("enter a intger greater than 1");

			} else if(m.getText().equals("L1Distance-Segmentation"))
			{

				segmentationStep = chooseInputWindow("enter a intger greater than 1");

			} else if(m.getText().equals("HSI-Intersection-Segmentation") ||
					m.getText().equals("HSI-L1Distance-Segmentation"))
			{

				check((Image) jListFiles.getSelectedValue());

			} else if(m.getText().equals("HSI-algorithm.Intersection"))
			{

				Image currentImg = (Image) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					jListFilesRanked.setListData(hsiIntersection.apply(currentImg, imagesList, segmentationStep));
				}

			} else if(m.getText().equals("HSI-L1Distance"))
			{

				Image currentImg = (Image) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					jListFilesRanked.setListData(hsiL1Distance.apply(currentImg, imagesList, segmentationStep));
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
				Image currentImg = (Image) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					Integer dominantColours = chooseInputWindow("Choose Number of dominant Colors");

					jListFilesRanked.setListData(
							hsiEuclidDistance.applySimilarity(currentImg, imagesList, dominantColours, similarity));
				}

			} else if(m.getText().equals("HSI-Chi-Square-Semi-Pseudo-Distance"))
			{

				Image currentImg = (Image) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					jListFilesRanked.setListData(chiSquare.apply(currentImg, imagesList, similarity));
				}
			} else if(m.getText().equals("NRA-Algorithm"))
			{

				Image currentImg = (Image) jListFiles.getSelectedValue();
				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					nra = true;
					Integer numberK = chooseInputWindow("Choose K");

					hsiEuclidDistance.applySimilarity(currentImg, imagesList, numberK, similarity);
					NRA_Algorithm_Sort[] euclidean = hsiEuclidDistance.getNRA_Values();
					chiSquare.apply(currentImg, imagesList, numberK);
					NRA_Algorithm_Sort[] chisq = chiSquare.getNRA_Values();
					jListFilesRanked = new JList();
					jListFilesRanked.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					jListFilesRanked.setListData(nraAlgorithm.calculate(euclidean, chisq, numberK));
					rightPanel.getViewport().setView(jListFilesRanked);
				}
			}
		}
	}

	private Integer chooseInputWindow(String text)
	{
		JFrame frame = new JFrame("Input Dialog");

		String segmentation = JOptionPane.showInputDialog(frame, text);

		return Integer.parseInt(segmentation);
	}

	/**
	 * Refreshes the GUI after new test data has been loaded
	 */
	private void refreshGUI()
	{
		imagesList = loadTestData(openDirectoryChooser());

		jListFiles = new JList(imagesList);
		jListFiles.setCellRenderer(new ImageCellRenderer());
		jListFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		leftScrollPanel.getViewport().setView(jListFiles);

		logger.info("refreshGUI ---------");
	}

	private void displayHistogram()
	{
		Image currentImg = (Image) jListFiles.getSelectedValue();

		JDialog d = new JDialog(this, "Histogram: " + currentImg.toString());

		if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			if(rgb || hsi)
			{
				JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				rgb = false;
			} else
			{
				HistogramLabel h =
						new HistogramLabelGray(currentImg.getHistogramGray("/" + currentImg.toString()));
				d.add(h);
				d.setSize(256, 480);
				d.setResizable(false);
				d.setVisible(true);
			}
		} else if(currentImg.getImage().getType() == BufferedImage.TYPE_3BYTE_BGR)
		{
			if(rgb)
			{
				HistogramLabel h =
						new HistogramLabelRGB(currentImg.getHistogramRGB("/" + currentImg.toString()));
				d.add(h);
				d.setSize(512, 480);
				d.setResizable(false);
				d.setVisible(true);
				rgb = false;
			} else if(hsi)
			{
				String name = currentImg.toString();
				currentImg.generateHistogramHSI(name);
				HistogramLabel h = new HistogramLabelHSI(currentImg.getHistogramHSI(name));
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

	private void checkImage()
	{
		Vector<BufferedImage> segment;
		int segstep = 4;

		Image currentImg = (Image) jListFiles.getSelectedValue();
		if(currentImg != null)
		{
			segment = currentImg.generateRasterInGivenSteps(segstep);
			displayHistogramSegment(segment);
		}

	}

	private void check(Image image)
	{
		if(image.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
		} else
		{
			segmentationStep = chooseInputWindow("enter a intger greater than 1");
		}

	}

	private void displayHistogramSegment(Vector<BufferedImage> segment)
	{
		Image currentImg = (Image) jListFiles.getSelectedValue();

		if(currentImg != null)
		{
			int segStep = 4;
			String name = currentImg.toString();
			List histogram = new ArrayList<>();
			JLabel help = new JLabel();
			JDialog d = new JDialog(this, "Histogram: " + currentImg.toString());
			float[] hseg;
			float[][][] h1seg;
			int x = 0;
			int y = 0;
			int z = 0;

			for(int i = 0; i < segment.size(); i++)
			{
				Image segmentImage = new Image(currentImg.getFilePath(), segment.get(i));

				if(currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{

				} else if(currentImg.getImage().getType() == BufferedImage.TYPE_3BYTE_BGR)
				{


				}

				//TODO check if file was already saved before generating + saving
				if(rgb)
				{
					segmentImage.generateHistogramRGB(name + segStep + "Seg" + i + "RGB");
					float[][][] hist1seg = segmentImage.getHistogramRGB(name + segStep + "Seg" + i + "RGB");
					histogram.add(hist1seg);
				} else if(hsi)
				{
					segmentImage.generateHistogramHSI(name + segStep + "Seg" + i + "HSI");
					float[][][] hist1seg = segmentImage.getHistogramHSI(name + segStep + "Seg" + i + "HSI");
					histogram.add(hist1seg);
				} else if(gray)
				{
					segmentImage.generateHistogramGray(name + segStep + "Seg" + i + "GRAY");
					float[] hist1seg = segmentImage.getHistogramGray(name + segStep + "Seg" + i + "GRAY");
					histogram.add(hist1seg);
				}
			}

			for(Object aHist : histogram)
			{
				if(gray)
				{
					hseg = (float[]) aHist;
					HistogramLabel h1 = new HistogramLabelGray(hseg);
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
					HistogramLabel h1 = new HistogramLabelRGB(h1seg);
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
					HistogramLabel h1 = new HistogramLabelHSI(h1seg);
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
}