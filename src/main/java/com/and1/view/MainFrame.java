package com.and1.view;

import com.and1.algorithm.Chi_Square_Semi_Pseudo_Distance;
import com.and1.algorithm.Euclidean_Distance_HSI;
import com.and1.algorithm.Intersection.IntersectionGRAY;
import com.and1.algorithm.Intersection.IntersectionHSI;
import com.and1.algorithm.Intersection.IntersectionRGB;
import com.and1.algorithm.L1Distance.L1DistanceHSI;
import com.and1.algorithm.NRA_Algorithm;
import com.and1.algorithm.SimilarityAlgorithm;
import com.and1.algorithm.sort.NRA_Algorithm_Sort;
import com.and1.model.img.Image;
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
import java.util.Properties;

import static com.and1.view.HistogramView.*;

//import com.and1.algorithm.L1Distance.L1Distance;

class MainFrame extends JFrame implements ActionListener
{
	private static final Logger logger = LogManager.getLogger(MainFrame.class);

	private JScrollPane leftScrollPanel;

	private JScrollPane rightPanel;

	private JList jListFiles;

	private JList<Object> jListFilesRanked;

	private List<Image> imagesList;

	private int segmentationStep;

	private Boolean rgb, hsi, gray;

	private JLabel statusBarLabel;

	private final IntersectionRGB intersectionRGB = new IntersectionRGB();

	//	private final L1Distance l1distance = new L1Distance();
	private final L1DistanceHSI hsiL1Distance = new L1DistanceHSI();

	private final Euclidean_Distance_HSI hsiEuclidDistance = new Euclidean_Distance_HSI();

	private final Chi_Square_Semi_Pseudo_Distance chiSquare = new Chi_Square_Semi_Pseudo_Distance();

	private final NRA_Algorithm nraAlgorithm = new NRA_Algorithm();

	Properties properties;

	MainFrame(ImageSplashScreen splash, Thread splashThread, Properties properties) throws HeadlessException
	{
		super("Image Recognition");

		this.properties = properties;

		File pathToImage;

		if (Boolean.parseBoolean(properties.getProperty("SelectImagePath")) == true)
		{
			pathToImage = openDirectoryChooser();
		}
		else
		{

			String pathToImageString = properties.getProperty("PathToImages");

			pathToImage = new File(pathToImageString);
		}

		imagesList = loadTestData(pathToImage);

		if (imagesList != null)
		{
			rgb = false;
			hsi = false;
			gray = false;

			splash.setMessage("Initializing GUI...");

			createGUI();
			statusBarLabel.setText("Total number of loaded imagesList: " + imagesList.size());

			logger.info("Total number of loaded imagesList: " + imagesList.size());

			splash.dispose();
			splashThread.interrupt();
		}
		else
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

		fileChooser.setCurrentDirectory(new File("F:/Programmierung/JAVA/ImageRecognition/ImagesGray"));

		int returnVal = fileChooser.showDialog(this, "Select Path");

		File file = null;

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			file = fileChooser.getSelectedFile();

			logger.info("Selected Path: " + file.getAbsolutePath());
		}

		return file;
	}

	private List<Image> loadTestData(File pathToImage)
	{
		List<Image> imageList = null;
		// load all jpg and create their histograms if the directory exists
		if (pathToImage != null && pathToImage.exists())
		{
			File[] fileList = pathToImage.listFiles();

			if (fileList != null)
			{
				imageList = new ArrayList<>();
				BufferedImage image;

				Image mrImage;
				for (File file : fileList)
				{
					if (file.toString().toLowerCase().endsWith("jpg") && file.canRead())
					{
						image = getBufferedImageFromFile(file);

						if (image != null)
						{
							mrImage = new Image(file.getAbsoluteFile(), image);
							imageList.add(mrImage);
						}
					}
				}

				logger.info("Total number of loaded imagesList: " + imageList.size());
			}
			else
			{
				JOptionPane.showMessageDialog(null, "No images in path:\n" + pathToImage.getAbsolutePath(), "Error",
											  JOptionPane.ERROR_MESSAGE);
			}
		}

		return imageList;
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

		btnPreview.addActionListener(e -> function.actionPreview((Image) jListFiles.getSelectedValue(), this));

		btnPreview.setToolTipText("Displays a full-sized preview of the currently selected image");
		btnPreview.setMnemonic(KeyEvent.VK_P);

		JButton segmentButton = new JButton("Segmentation");
		segmentButton.addActionListener(e -> function.actionSegmentation(this, (Image) jListFiles.getSelectedValue()));
		segmentButton.setMnemonic(KeyEvent.VK_P);

		leftCmdPanel.add(btnPreview);
		leftCmdPanel.add(segmentButton);

		// list for displaying the test data imgList
		jListFiles = new JList<>(imagesList.toArray());
		jListFiles.setCellRenderer(new ImageCellRenderer());
		jListFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		leftScrollPanel = new JScrollPane(jListFiles);
		leftSplitter.add(leftCmdPanel);
		leftSplitter.add(leftScrollPanel);

		// list for displaying the sorted list of imgList
		jListFilesRanked = new JList<>();
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
		}
		catch (IOException e)
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

		JMenuItem intersect = new JMenuItem("Intersection - RGB");
		intersect.addActionListener(this);
		menuAlgorithms.add(intersect);

		JMenuItem intersectGray = new JMenuItem("Intersection - Gray");
		intersectGray.addActionListener(this);
		menuAlgorithms.add(intersectGray);

		JMenuItem hsiintersect = new JMenuItem("Intersection - HSI");
		hsiintersect.addActionListener(this);
		menuAlgorithms.add(hsiintersect);

		JMenuItem l1Distance = new JMenuItem("L1-Distance");
		l1Distance.addActionListener(this);
		menuAlgorithms.add(l1Distance);

		JMenuItem hsil1dist = new JMenuItem("HSI-L1Distance");
		hsil1dist.addActionListener(this);
		menuAlgorithms.add(hsil1dist);

		JMenuItem segintersect = new JMenuItem("IntersectionRGB-Segmentation");
		segintersect.addActionListener(this);
		menuAlgorithms.add(segintersect);

		JMenuItem segl1dist = new JMenuItem("L1Distance-Segmentation");
		segl1dist.addActionListener(this);
		menuAlgorithms.add(segl1dist);

		JMenuItem hsisegintersect = new JMenuItem("HSI-IntersectionRGB-Segmentation");
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
	 * Refreshes the GUI after new test data has been loaded
	 */
	private void refreshGUI()
	{
		imagesList = loadTestData(openDirectoryChooser());

		jListFiles = new JList<>(imagesList.toArray());
		jListFiles.setCellRenderer(new ImageCellRenderer());
		jListFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		leftScrollPanel.getViewport().setView(jListFiles);

		logger.info("refreshGUI ---------");
	}

	private void checkImage()
	{
		List<BufferedImage> segment;
		int segstep = 4;

		Image currentImg = (Image) jListFiles.getSelectedValue();
		if (currentImg != null)
		{
			segment = currentImg.generateRasterInGivenSteps(segstep);
			displayHistogramSegment(segment);
		}

	}

	private void displayHistogramSegment(List<BufferedImage> segment)
	{
		Image currentImg = (Image) jListFiles.getSelectedValue();

		if (currentImg != null)
		{
			int segStep = 4;
			String name = currentImg.toString();
			List<Object> histogram = new ArrayList<>();
			JLabel help = new JLabel();
			JDialog d = new JDialog(this, "Histogram: " + currentImg.toString());
			float[] hseg;
			float[][][] h1seg;
			int x = 0;
			int y = 0;
			int z = 0;

			for (int i = 0; i < segment.size(); i++)
			{
				Image segmentImage = new Image(currentImg.getFilePath(), segment.get(i));

				//TODO check if file was already saved before generating + saving
				if (rgb)
				{
//					segmentImage.generateHistogramRGB(name + segStep + "Seg" + i + "RGB");
//					float[][][] hist1seg = segmentImage.getHistogramRGB(name + segStep + "Seg" + i + "RGB");
//					histogram.add(hist1seg);
				}
				else if (hsi)
				{
//					segmentImage.generateHistogramHSI(name + segStep + "Seg" + i + "HSI");
//					float[][][] hist1seg = segmentImage.getHistogramHSI(name + segStep + "Seg" + i + "HSI");
//					histogram.add(hist1seg);
				}
				else if (gray)
				{
//					segmentImage.generateHistogramGray(name + segStep + "Seg" + i + "GRAY");
//					float[] hist1seg = segmentImage.getHistogramGray(name + segStep + "Seg" + i + "GRAY");
//					histogram.add(hist1seg);
				}
			}

			for (Object aHist : histogram)
			{
				if (gray)
				{
					hseg = (float[]) aHist;
					HistogramLabel h1 = new HistogramLabelGray(hseg);
					h1.setSize(256, 480);
					h1.setLocation(x, y);
					d.add(h1);

					if (z == 1)
					{
						x = 0;
						y += 480;
						z = 0;
					}
					else
					{
						x += 256;
						z++;
					}

					d.setSize(512, 980);

				}
				else if (rgb)
				{
					h1seg = (float[][][]) aHist;
					HistogramLabel h1 = new HistogramLabelRGB(h1seg);
					h1.setSize(512, 480);
					h1.setLocation(x, y);
					d.add(h1);
					if (z == 1)
					{
						x = 0;
						y += 480;
						z = 0;
					}
					else
					{
						x += 512;
						z++;
					}
					d.setSize(1024, 980);

				}
				else if (hsi)
				{
					h1seg = (float[][][]) aHist;
					HistogramLabel h1 = new HistogramLabelHSI(h1seg);
					h1.setSize(162, 480);
					h1.setLocation(x, y);
					d.add(h1);
					if (z == 1)
					{
						x = 0;
						y += 480;
						z = 0;
					}
					else
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
		if (src instanceof JMenuItem)
		{
			JMenuItem m = (JMenuItem) src;
			jListFilesRanked = new JList<>();
			jListFilesRanked.setCellRenderer(new ImageCellRendererSorted());
			jListFilesRanked.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			rightPanel.getViewport().setView(jListFilesRanked);

			SimilarityAlgorithm similarityAlgorithm;
			if (m.getText().equals("Intersection - RGB"))
			{
				Image currentImg = (Image) jListFiles.getSelectedValue();

				//TODO check if in original code segmentationStep has value != 0
				// read number and convert it to a power of 2
				logger.info("segmentation step: " + segmentationStep);

				similarityAlgorithm = new IntersectionRGB();

				jListFilesRanked
						.setListData(similarityAlgorithm.calculateSimilarity(currentImg, imagesList, segmentationStep)
											 .toArray());
			}
			else if (m.getText().equals("Intersection - Gray"))
			{
				Image currentImg = (Image) jListFiles.getSelectedValue();

				//TODO check if in original code segmentationStep has value != 0
				// read number and convert it to a power of 2
				logger.info("segmentation step: " + segmentationStep);

				similarityAlgorithm = new IntersectionGRAY();

				List<Image> imageList =
						similarityAlgorithm.calculateSimilarity(currentImg, imagesList, segmentationStep);

				jListFilesRanked.setListData(imageList.toArray());
			}
			else if (m.getText().equals("Intersection - HSI"))
			{
				Image currentImg = (Image) jListFiles.getSelectedValue();

				similarityAlgorithm = new IntersectionHSI();

				jListFilesRanked
						.setListData(similarityAlgorithm.calculateSimilarity(currentImg, imagesList, segmentationStep)
											 .toArray());
			}
			else if (m.getText().equals("L1-Distance"))
			{

				Image currentImg = (Image) jListFiles.getSelectedValue();
//				jListFilesRanked.setListData(
//						l1distance.calculateSimilarity(currentImg, imagesList, segmentationStep).toArray());
			}
			else if (m.getText().equals("GrayScale"))
			{
				displayHistogramGRAY(this, (Image) jListFiles.getSelectedValue());

			}
			else if (m.getText().equals("RGB"))
			{
				displayHistogramRGB(this, (Image) jListFiles.getSelectedValue());
			}
			else if (m.getText().equals("HSI"))
			{
				displayHistogramHSI(this, (Image) jListFiles.getSelectedValue());
			}
			else if (m.getText().equals("IntersectionRGB-Segmentation"))
			{
				segmentationStep = chooseInputWindow("enter a intger greater than 1");

			}
			else if (m.getText().equals("L1Distance-Segmentation"))
			{

				segmentationStep = chooseInputWindow("enter a intger greater than 1");

			}
			else if (m.getText().equals("HSI-IntersectionRGB-Segmentation") ||
					m.getText().equals("HSI-L1Distance-Segmentation"))
			{

				check((Image) jListFiles.getSelectedValue());

			}
			else if (m.getText().equals("HSI-L1Distance"))
			{

				Image currentImg = (Image) jListFiles.getSelectedValue();
				if (currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					jListFilesRanked
							.setListData(hsiL1Distance.calculateSimilarity(currentImg, imagesList, segmentationStep)
												 .toArray());
				}

			}
			else if (m.getText().equals("Segmentation-Gray"))
			{

				gray = true;
				checkImage();
			}
			else if (m.getText().equals("Segmentation-HSI"))
			{

				hsi = true;
				checkImage();
			}
			else if (m.getText().equals("HSI-Euclidean-Distance"))
			{
				Image currentImg = (Image) jListFiles.getSelectedValue();
				if (currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					Integer dominantColours = chooseInputWindow("Choose Number of dominant Colors");

					jListFilesRanked.setListData(
							hsiEuclidDistance.applySimilarity(currentImg, imagesList, dominantColours, similarity)
									.toArray());
				}

			}
			else if (m.getText().equals("HSI-Chi-Square-Semi-Pseudo-Distance"))
			{

				Image currentImg = (Image) jListFiles.getSelectedValue();
				if (currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					jListFilesRanked
							.setListData(chiSquare.calculateSimilarity(currentImg, imagesList, similarity).toArray());
				}
			}
			else if (m.getText().equals("NRA-Algorithm"))
			{

				Image currentImg = (Image) jListFiles.getSelectedValue();
				if (currentImg.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
				{
					JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{

					Integer numberK = chooseInputWindow("Choose K");

					hsiEuclidDistance.applySimilarity(currentImg, imagesList, numberK, similarity);
					NRA_Algorithm_Sort[] euclidean = hsiEuclidDistance.getNRA_Values();
					chiSquare.calculateSimilarity(currentImg, imagesList, numberK);
					NRA_Algorithm_Sort[] chisq = chiSquare.getNRA_Values();
					jListFilesRanked = new JList<>();
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

	private void check(Image image)
	{
		if (image.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY)
		{
			JOptionPane.showMessageDialog(null, "no RGB image ", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			segmentationStep = chooseInputWindow("enter a intger greater than 1");
		}

	}
}