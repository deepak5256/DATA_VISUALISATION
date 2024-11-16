package com.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.awt.event.ComponentAdapter; // Import added
import java.awt.event.ComponentEvent; // Import added
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;


public class DataVisualizationGUI extends JFrame {

    private JComboBox<String> datasetComboBox;
    private JComboBox<String> visualizationComboBox;
    private JButton showStorytellingButton;
    private JButton showVisualizationButton;
    private JButton showDashboardButton;
    private JButton showTableButton;
    private JPanel mainPanel;
    private JPanel contentPanel; // Single panel for content
    private Map<String, Map<String, Double[]>> datasets; // To hold multiple columns of data
    private Map<String, String[][]> datasetTables; // To hold dataset tables
    private JLabel imageLabel;

    public DataVisualizationGUI() {
        // Set UI Manager properties for modern look
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD | Font.ITALIC, 16));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.BOLD | Font.ITALIC, 16));
        UIManager.put("Label.font", new Font("Segoe UI", Font.BOLD | Font.ITALIC, 16));

        // Initialize components
        datasetComboBox = createStyledComboBox(new String[]{"Dataset 1"});
        showStorytellingButton = createStyledButton("ABOUT DATASET CHOSEN"); // Initialize the storytelling button
        visualizationComboBox = createStyledComboBox(new String[]{"Bar Chart", "Line Chart", "Area Chart",   "Pie Chart"});
        showVisualizationButton = createStyledButton("Show Visualization");
        showDashboardButton = createStyledButton("Sample Dashboard");
        showTableButton = createStyledButton("Show Table");

        datasets = new HashMap<>();
        datasetTables = new HashMap<>();
        
        loadCSVData("/home/deepak/Desktop/my-project (4)/src/main/resources/dataset1.csv", "Dataset 1");
       

        // Set up panels
        mainPanel = new JPanel();
        contentPanel = new JPanel(); // Single panel for content

        // Configure the main panel
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(datasetComboBox);
        mainPanel.add(showStorytellingButton);
        mainPanel.add(visualizationComboBox);
        mainPanel.add(showVisualizationButton);
        mainPanel.add(showDashboardButton);
        mainPanel.add(showTableButton);

        // Configure contentPanel
        contentPanel.setLayout(new CardLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY); // Set background color for a modern look

        // Configure frame
        setTitle("Data Visualization");
        setSize(1200, 800); // Increased size to accommodate larger buttons and content
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Add action listeners
        showVisualizationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContent("visualization");
            }
        });

        showDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContent("dashboard");
            }
        });

        showTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContent("table");
            }
        });
        
        showStorytellingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContent("storytelling");
            }
        });
    }



    // Method to load CSV data into the datasets map and datasetTables map
    private void loadCSVData(String csvFilePath, String datasetName) {
        Map<String, Double[]> data = new HashMap<>();
        String[][] tableData = new String[50][6]; 
        int rowIndex = 0;

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] line;
            reader.readNext(); // Skip header
            while ((line = reader.readNext()) != null) {
                String column1 = line[1]; 
                Double column2 = Double.parseDouble(line[2]);
                Double column3 = Double.parseDouble(line[3]);
                Double column4 = Double.parseDouble(line[4]); 
                Double column5 = Double.parseDouble(line[5]); 
                
                data.put(state, new Double[]{activeCases, curedCases, deaths, totalConfirmed});

                // Populate table data
                tableData[rowIndex] = line;
                rowIndex++;
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        
        datasets.put(datasetName, data);
        datasetTables.put(datasetName, tableData);
    }

    private JFreeChart createChart(String visualizationType, Map<String, Double[]> data, int columnIndex) {
    JFreeChart chart = null;
    
    switch (visualizationType) {
        case "Bar Chart":
            CategoryDataset barDataset = createDataset(data, columnIndex);
            chart = ChartFactory.createBarChart(
                "Agriculture Crop Production In India \nCost of Cultivation (/Hectare)[A2+FL]",
                "State/UT",                   
                "Cost of Cultivation (/Hectare) ",            
                barDataset,                   
                PlotOrientation.HORIZONTAL,   
                true,                         
                true,                         
                true                          
            );

            
            chart.setBackgroundPaint(Color.WHITE);  
            chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 24)); // Title font
            chart.getTitle().setPaint(Color.DARK_GRAY); 

            // Customize the plot
            CategoryPlot plot = chart.getCategoryPlot(); 
            plot.setBackgroundPaint(Color.LIGHT_GRAY); 
            plot.setDomainGridlinePaint(Color.WHITE);  
            plot.setRangeGridlinePaint(Color.WHITE);   

            // Customize axis fonts and colors
            plot.getDomainAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 14)); 
            plot.getRangeAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 14));  
            plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12)); 
            plot.getDomainAxis().setTickLabelPaint(Color.DARK_GRAY); 
            plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12)); 
            plot.getRangeAxis().setTickLabelPaint(Color.DARK_GRAY); 

 
            chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 14)); 
            chart.getLegend().setBackgroundPaint(Color.WHITE); 
            break;

             case "Line Chart":
            CategoryDataset lineDataset = createDataset(data, columnIndex);
            chart = ChartFactory.createLineChart(
                "Agriculture Crop Production In India \n[C2'cost of cultivation/Hector']",  
                "State",                  
                "Cost of Cultivation (/Hectare)",           
                lineDataset,                  
                org.jfree.chart.plot.PlotOrientation.VERTICAL, 
                true,                        
                true,                         
                true                        
            );
            break;
            
            
        case "Area Chart":
            CategoryDataset areaDataset = createDataset(data, columnIndex);
            chart = ChartFactory.createAreaChart(
                "Agriculture Crop Production In India \n[of Sugarcane,Wheat and Rapeseed mustard ]c2",  e
                "State/UT",                   
                "Cost of Cultivation (/Quintal)c2",           
                areaDataset,                  
                org.jfree.chart.plot.PlotOrientation.VERTICAL, 
                true,                         
                true,                        
                true                        
            );
            break;
        
    /*    case "Scatter Plot":
            chart = ChartFactory.createScatterPlot(
                "Agriculture Crop Production In India [Key Insights from Maximum Crop Cultivation Metrics",
                "State/UT",                  
                "Cost of Cultivation (/Hectare)",         
                createScatterDataset(data),   
                PlotOrientation.VERTICAL,   
                true,                         
                true,                         
                true                        
            );
            customizeChart(chart, "Scatter Plot");
            break;
        
        case "Histogram":
            chart = ChartFactory.createHistogram(
                "Agriculture Crop Production In India [Key Insights from Maximum Crop Cultivation Metrics", 
                "State/UT",           
                "Cost of Cultivation (/Hectare)",                 
                createHistogramDataset(data),
                PlotOrientation.VERTICAL,    
                true,                       
                true,                       
                true                       
            );
            customizeChart(chart, "Histogram");
            break;
            */
        
   case "Pie Chart":
            PieDataset pieDataset = createPieDataset(data, columnIndex);
            chart = ChartFactory.createPieChart(
                "Agriculture Crop Production In India\n Yield (Quintal/ Hectare) of Sugarcane,Wheat ",  
                pieDataset,                         
                true,                                
                true,                               
                true                               
            );
            break;
        // Add more cases if needed
    }


    return chart;
}

private void customizeChart(JFreeChart chart, String chartType) {
    chart.setBackgroundPaint(Color.decode("#ddb8a6"));
// Set chart background color
    chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 24)); 
    chart.getTitle().setPaint(Color.DARK_GRAY); // Title color

    if (chartType.equals("Bar Chart") || chartType.equals("Line Chart") || chartType.equals("Area Chart")) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY); // Set plot background color
        plot.setDomainGridlinePaint(Color.WHITE);  // Domain grid lines
        plot.setRangeGridlinePaint(Color.WHITE);   // Range grid lines
        plot.getDomainAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 14));  // X-axis label font
        plot.getRangeAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 14));   // Y-axis label font
        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12)); // X-axis tick font
        plot.getDomainAxis().setTickLabelPaint(Color.DARK_GRAY); // X-axis tick color
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 12)); // Y-axis tick font
        plot.getRangeAxis().setTickLabelPaint(Color.DARK_GRAY); // Y-axis tick color
        chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 14)); // Legend font
        chart.getLegend().setBackgroundPaint(Color.WHITE); // Legend background
    }
}

    private CategoryDataset createDataset(Map<String, Double[]> data, int columnIndex) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    for (Map.Entry<String, Double[]> entry : data.entrySet()) {
        dataset.addValue(entry.getValue()[columnIndex], "Metric", entry.getKey());
    }

    return dataset;
}

private XYDataset createScatterDataset(Map<String, Double[]> data) {
    DefaultXYDataset dataset = new DefaultXYDataset();
    double[][] seriesData = new double[2][data.size()]; // X and Y series

    int i = 0;
    for (Map.Entry<String, Double[]> entry : data.entrySet()) {
        seriesData[0][i] = i; // X-axis values
        seriesData[1][i] = entry.getValue()[0]; // Y-axis values
        i++;
    }

    dataset.addSeries("Scatter Plot Data", seriesData);
    return dataset;
}

private HistogramDataset createHistogramDataset(Map<String, Double[]> data) {
    HistogramDataset dataset = new HistogramDataset();
    double[] values = data.values().stream()
                            .flatMapToDouble(arr -> Arrays.stream(arr).mapToDouble(Double::doubleValue))
                            .toArray();
    dataset.addSeries("Frequency", values, 20); // 10 bins, adjust as needed
    return dataset;
}


private PieDataset createPieDataset(Map<String, Double[]> data, int columnIndex) {
    DefaultPieDataset dataset = new DefaultPieDataset<>();

    for (Map.Entry<String, Double[]> entry : data.entrySet()) {
        dataset.setValue(entry.getKey(), entry.getValue()[columnIndex]);
    }

    return dataset;
}

    private void showContent(String contentType) {
    contentPanel.removeAll();

    String selectedDataset = (String) datasetComboBox.getSelectedItem();
    Map<String, Double[]> data = datasets.get(selectedDataset);

    switch (contentType) {
        case "storytelling":
            JTextArea storyTextArea = new JTextArea();
            storyTextArea.setText("The Journey of Indian Agriculture: A Story of Data and Discovery\n\nIn the heart of India, where the fields stretch endlessly and the soil whispers ancient tales, a curious data scientist embarked on a quest to uncover the story of Indian agriculture. Driven by a passion for data and a love for the land, they set out to explore the agricultural production landscape of their homeland.\n\nTheir journey began with a treasure trove of data found on Kaggle, titled Agriculture Crops Production in India. This dataset, curated by Srinivas, offered a comprehensive view of various crops produced across different states of India over several years. It included vital information on crop types, production volumes, and the states where these crops thrived.\n\nThe goal was to visualize and understand the dynamics of crop production across India. The dataset contained columns such as State, Crop, Production, and Year. With this information, they could piece together how each crop fared in different regions and how production trends evolved over time.\nTo bring this story to life, they created a series of compelling visualizations. The project began with a dashboard that provided an overview of crop production trends. A scatter plot was used to highlight the correlation between production volumes and states, while a pie chart showed the proportion of different crops produced across India. Line and area charts illustrated the changes in production over the years, and a text box provided detailed explanations of these visualizations.\nAs the analysis unfolded, fascinating patterns emerged. It was observed that Punjab and Haryana consistently led in wheat production, while Tamil Nadu and Karnataka were renowned for their rice cultivation. The scatter plot revealed unique crop production profiles in the northeastern states compared to the rest of the country. The pie chart demonstrated that rice and wheat dominated the Indian agricultural landscape, reflecting the staple diet of the nation.\nOne of the most enlightening discoveries was how the line and area charts displayed the impact of monsoon patterns on crop production. Years of favorable rainfall led to bumper harvests, while drought years saw a significant dip in production. This insight highlighted the critical role of weather in agriculture and the resilience of Indian farmers.\nThe project was more than just a data analysis exercise; it was a celebration of India's agricultural heritage. The visualizations and interpretations shed light on the hard work and dedication of farmers who tilled the land and nurtured crops under challenging conditions. The project also underscored the importance of data in understanding and improving agricultural practices.\nIn the end, this journey through the data became a story of discovery, connection, and appreciation for the land that feeds a nation. Through these visualizations, the tale of Indian agriculture was brought to life, weaving together the past, present, and future of crop production in this beloved country.\n link:https://www.kaggle.com/datasets/srinivas1/agricuture-crops-production-in-india\n\nThe provided table outlines agricultural data for various crops across different states in India, detailing the cost of cultivation and production, as well as crop yield per hectare. Key components of the table include:\n\n\nCrop: The table covers multiple crops, including Arhar (pigeon pea), Cotton, Gram (chickpea), Groundnut, Maize, Moong (green gram), Paddy (rice), Rapeseed & Mustard, Sugarcane, and Wheat.\n\nState: Each crop is grown in specific states, including Uttar Pradesh, Karnataka, Gujarat, Andhra Pradesh, Maharashtra, and others, reflecting regional agricultural practices.\n\nCost of Cultivation (A2+FL): This metric represents the paid-out expenses (A2) plus the value of family labor (FL) involved in cultivating the crop per hectare. Costs vary based on the state and crop, with values ranging from ₹5,483.54 to ₹66,335.06 per hectare.\n\nCost of Cultivation (C2): The C2 metric adds imputed rent and interest on the value of owned land and capital to A2+FL. This is a broader measure of cultivation cost, showing variations across states.\n\nCost of Production (C2): This reflects the cost of producing one quintal of the crop, including inputs like seeds, labor, and machinery. It ranges from ₹86.53 to ₹5,777.48 per quintal depending on the crop and state.\n\nYield (Quintal/Hectare): The yield represents the crop output per hectare, with figures ranging from low-yielding crops like Moong (1.32 quintals/hectare in Karnataka) to high-yielding crops like Sugarcane (986.21 quintals/hectare in Karnataka).\n\nThe data provides valuable insights into the regional disparities in agricultural input costs, productivity, and efficiency across states and crops.");
            storyTextArea.setEditable(false);
            storyTextArea.setLineWrap(true);
            storyTextArea.setWrapStyleWord(true);
            storyTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            storyTextArea.setBackground(Color.WHITE);
            JScrollPane storytellingScrollPane = new JScrollPane(storyTextArea); // Renamed variable
            storytellingScrollPane.setBorder(BorderFactory.createTitledBorder("Storytelling"));
            contentPanel.add(storytellingScrollPane, "storytelling");
            break;
        
        case "visualization":
            String selectedVisualization = (String) visualizationComboBox.getSelectedItem();
            int columnIndex = getColumnIndexForVisualization(selectedVisualization);

            if (data != null) {
                JFreeChart chart = createChart(selectedVisualization, data, columnIndex);
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(800, 600));

                // Create a text area or label for the chart explanation
                JTextArea chartExplanation = new JTextArea();
                chartExplanation.setEditable(false);
                chartExplanation.setLineWrap(true);
                chartExplanation.setWrapStyleWord(true);
                chartExplanation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chartExplanation.setBackground(Color.WHITE);

                switch (selectedVisualization) {
                    case "Bar Chart":
                        chartExplanation.setText("This visualization shows is a horizontal bar chart comparing agricultural crop production across different states in India. The chart is titled Agriculture Crop Production in India (Key Insights from Maximum Crop Cultivation Metrics).\nFrom the data presented, we can observe the following key insights:\nTamil Nadu appears to have the highest agricultural crop production among the states shown, with its bar extending the furthest to the right.\n Maharashtra follows closely as the second-highest producer.\nKarnataka and Andhra Pradesh also show significant crop production, with their bars extending quite far.\nStates like Bihar, Gujarat, and Madhya Pradesh have comparatively lower crop production metrics, as indicated by their shorter bars.\nUttar Pradesh, Rajasthan, and Punjab fall in the middle range of production compared to the other states shown.\nThe scale at the top of the chart goes up to 70,000.\nAll bars are colored in a light red or pink shade, making it easy to compare lengths across states.\nThis visualization provides a quick comparative view of crop production capabilities across major agricultural states in India, highlighting regional differences in agricultural output. It's important to note that this data represents maximum crop cultivation metrics, which may not reflect average or typical production levels.\nIn agricultural economics, A2+FL is a component used to calculate the Cost of Cultivation for crops. It stands for:\nA2: Includes all paid-out expenses incurred by the farmer, such as the cost of seeds, fertilizers, irrigation, hired labor, machinery, fuel, and other inputs directly required for farming activities.\nFL: Refers to the value of Family Labor, which accounts for the work done by family members without direct monetary compensation. This labor is still an essential contribution to the farming process.\nThe A2+FL cost is a more comprehensive measure than just the A2 component (which considers only paid-out costs), as it factors in the non-cash cost of family labor, offering a more realistic view of the total cultivation expenses from the farmer's perspective.");
                        break;
                    case "Line Chart":
                        chartExplanation.setText("This visualization shows is a line graph titled Agriculture Crop Production In India 'cost of cultivation/Hector' [Key Insights from Maximum Crop Cultivation Metrics].\nKey observations:\nThe x-axis represents different Indian states, from Bihar to Orissa.\nThe y-axis shows 'theCost of Cultivation (/Hectare)', ranging from 0 to 90,000.\nThe data is represented by a single red line connecting data points for each state.\nThere are significant fluctuations in the cost of cultivation across different states.\nThe highest peaks appear to be for Andhra Pradesh, Tamil Nadu, Karnataka, and Maharashtra, all reaching close to or above 80,000.\nStates like Bihar, Gujarat, and Madhya Pradesh show lower costs of cultivation, around 20,000 or less.\nThere's a clear pattern of alternating high and low costs across the states, creating a jagged appearance in the graph.\nPunjab, Rajasthan, and Uttar Pradesh show moderate costs compared to the extremes.\nThe graph effectively visualizes the variability in cultivation costs across Indian states.\nThis representation allows for quick comparison of cultivation costs between different regions, highlighting economic disparities in Indian agriculture.\nC2 is a more comprehensive measure used in agricultural economics to calculate the Cost of Cultivation for crops");
                        break;
                    case "Area Chart":
                        chartExplanation.setText("This visualization shows is area chart titled Agriculture Crop Production in India (Key Insights from Maximum Crop Cultivation Metrics).\nKey observations:\nThe chart displays crop production data for various Indian states along the x-axis.\nThe y-axis scale ranges from 0 to 1,800, likely representing production in some standard unit (Quintal).\nThere are significant variations in crop production across states, shown by the peaks and troughs in the filled area.\nGujarat shows the highest peak, reaching close to the 1,800 mark on the y-axis.\nHaryana has the second-highest peak, reaching about 1,200 on the y-axis.\nStates like Bihar, Madhya Pradesh, and Tamil Nadu show relatively lower production levels.\nThe area under the curve is colored in a bright red, contrasting with the gray background.\nThere's a pattern of alternating high and low production levels across the states, creating a jagged appearance.\nThe x-axis lists 13 states, from Bihar to Orissa, with all labels clearly visible.\nSome states like Punjab, Rajasthan, and Uttar Pradesh show moderate production levels compared to the extremes.\nThis visualization effectively highlights the variability in agricultural output across Indian states, providing insights into regional agricultural strengths and differences. The data likely represents maximum cultivation metrics rather than average production.\nC2 is a more comprehensive measure used in agricultural economics to calculate the Cost of Cultivation for crops. It includes all the components of the A2+FL cost, but with additional factors to provide a more complete estimate of farming expenses. Specifically, C2 accounts for:\nA2 Costs: All paid-out costs, including seeds, fertilizers, irrigation, hired labor, fuel, etc.FL (Family Labor): The imputed value of unpaid family labor.\nRent for Owned Land: The notional rent the farmer could have earned if they leased their land to others instead of using it for cultivation.\nInterest on Owned Capital Assets: The interest on investments in fixed assets such as machinery, equipment, and farm buildings.\nIn short, C2 is a broader calculation that includes not only the direct and family labor costs (A2+FL) but also the imputed rent on land and interest on capital assets, giving a fuller picture of the total economic cost of cultivation.");
                        break;
                    case "Pie Chart":
                        chartExplanation.setText("This visualization shows is a pie chart titled Agriculture Crop Production In India Yield (Quintal/ Hectare) [Key Insights from Maximum Crop Cultivation Metrics].\nKey observations:\nThe chart displays crop production yield data for various Indian states.\nEach state is represented by a different colored slice of the pie.\nThe largest slices belong to Karnataka (dark blue), Tamil Nadu (pink), and Maharashtra (yellow-green).\nSmaller slices represent states like Bihar, Gujarat, and Andhra Pradesh.\nThe chart includes labels for each state, positioned around the outside of the pie.\nThere's a color-coded legend at the bottom of the chart for easy reference.\nThe chart provides a clear visual comparison of relative crop yields across different states.\nSome smaller slices are difficult to discern, indicating lower yields in those states.\nThe use of contrasting colors makes it easy to distinguish between different states.\nThis visualization effectively highlights the disparities in agricultural productivity among Indian states.\n");
                        break;
                    case "Scatter Plot":
                        chartExplanation.setText("This image shows a scatter plot titled Agriculture Crop Production In India [Key Insights from Maximum Crop Cultivation Metrics].\nKey observations:\nThe x-axis represents different states or union territories (State/UT), ranging from 0 to 12.5.\nThe y-axis shows the Cost of Cultivation (/Hectare), ranging from 10,000 to 68,000.\nEach data point is represented by a small red square.\nThere are several high points, with the highest reaching about 66,000 on the y-axis.\nMost data points are clustered in the lower half of the chart, below the 40,000 mark.\nThere's a clear outlier at the top of the chart, significantly higher than other points.\nThe chart has a grid background, aiding in reading specific values.\nThere's a wide spread of data points across the x-axis, indicating variety among states.\nThe scatter plot effectively shows the distribution and range of cultivation costs.\nIt allows for identifying both trends and anomalies in crop production costs across India.\n");
                        break;
                    case "Histogram":
                        chartExplanation.setText("This image shows a HIstogram chart titled Agriculture Crop Production In India [Key Insights from Maximum Crop Cultivation Metrics].\nKey observations:\nThe x-axis represents different states or union territories (State/UT), ranging from 0 to 95,000.\nThe y-axis shows the Cost of Cultivation (/Hectare), ranging from 0 to 27.\nEach bar is colored in red, representing the frequency of crop production for each state.\nThe highest bar is at the far left, reaching almost 26 on the y-axis.\nThere are several other notable peaks, including ones around the 15,000 and 20,000 marks on the x-axis.\nMany bars are relatively short, indicating lower costs of cultivation in numerous states.\nThe chart has a grid background, making it easier to read specific values.\nThere's a clear variation in cultivation costs across different states/UTs.\nThe chart provides a visual representation of the economic aspects of crop production in India.\nIt allows for quick comparison of cultivation costs between different regions.\n");
                        break;    
                    default:
                        chartExplanation.setText("No explanation available for this chart.");
                        break;
                }

                JScrollPane explanationScrollPane = new JScrollPane(chartExplanation);
                explanationScrollPane.setBorder(BorderFactory.createTitledBorder("Chart Explanation"));

                JPanel visualizationPanel = new JPanel(new BorderLayout());
                visualizationPanel.add(chartPanel, BorderLayout.CENTER);
                visualizationPanel.add(explanationScrollPane, BorderLayout.SOUTH);

                contentPanel.add(visualizationPanel, "Visualization");
            } else {
                contentPanel.add(new JLabel("No data found for " + selectedDataset), "Visualization");
            }
            break;
        
        case "dashboard":
    if (data != null) {
        // Create each chart with the appropriate data
        JFreeChart barChart = createChart("Bar Chart", data, 0); 
        JFreeChart lineChart = createChart("Line Chart", data, 1); 
        JFreeChart areaChart = createChart("Area Chart", data, 2); 
      //  JFreeChart scatterplotChart = createChart("Scatter Plot", data, 2);
      //  JFreeChart histogramChart = createChart("Histogram", data, 1);
        JFreeChart pieChart = createChart("Pie Chart", data, 3); // Adjust column index for Pie Chart

        // Create ChartPanels for each chart
        ChartPanel barChartPanel = new ChartPanel(barChart);
        barChartPanel.setPreferredSize(new Dimension(800, 600));

        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        lineChartPanel.setPreferredSize(new Dimension(800, 600));

        ChartPanel areaChartPanel = new ChartPanel(areaChart);
        areaChartPanel.setPreferredSize(new Dimension(800, 600));
        
      //  ChartPanel scatterplotChartPanel = new ChartPanel(scatterplotChart);
      //  scatterplotChartPanel.setPreferredSize(new Dimension(800, 600));
        
      //  ChartPanel histogramChartPanel = new ChartPanel(histogramChart);
      //  histogramChartPanel.setPreferredSize(new Dimension(800, 600));

        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(800, 600));

        // Create a JPanel with a GridLayout to hold the charts
        JPanel dashboardContentPanel = new JPanel(new GridLayout(2, 2)); // Adjust layout as needed
        dashboardContentPanel.add(barChartPanel);
        dashboardContentPanel.add(lineChartPanel);
        dashboardContentPanel.add(areaChartPanel);
     //   dashboardContentPanel.add(scatterplotChartPanel);
     //   dashboardContentPanel.add(histogramChartPanel);
        dashboardContentPanel.add(pieChartPanel);

        // Add the dashboardContentPanel to the contentPanel
        contentPanel.add(dashboardContentPanel, "Dashboard");
    } else {
        contentPanel.add(new JLabel("No data found for " + selectedDataset), "Dashboard");
    }
    break;

        case "table":
                String[][] tableData = datasetTables.get(selectedDataset);

                if (tableData != null) {
                    String[] columnNames = {"Crop", "Name of State ", "Cost of Cultivation (/Hectare)A2+FL ", "Cost of Cultivation (/Hectare)C2", "Cost of Production (/Quintal)C2 ", "Yield (Quintal/ Hectare) "};
                    JTable table = new JTable(tableData, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);

                    JPanel tableContentPanel = new JPanel(new BorderLayout());
                    tableContentPanel.add(scrollPane, BorderLayout.CENTER);

                    contentPanel.add(tableContentPanel, "Table");
                } else {
                    contentPanel.add(new JLabel("No data found for " + selectedDataset), "Table");
                }
                break;
        }

    // Refresh the content panel to display the new content
    CardLayout cl = (CardLayout) contentPanel.getLayout();
    cl.show(contentPanel, contentType);
    
     revalidate();
     repaint();
}

// Method to get column index based on visualization type
    private int getColumnIndexForVisualization(String visualizationType) {
    switch (visualizationType) {
        case "Bar Chart":
            return 0;
        case "Line Chart":
            return 1; 
        case "Area Chart":
            return 2; 
        case "Pie Chart":
            return 3; 
        case "Scatter Plot":
            return 2;    
        case "Histogram":
            return 1;    
        default:
            return 0;
    }
}


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.decode("#008CBA"));
        button.setForeground(Color.ORANGE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 18));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JComboBox<String> createStyledComboBox(String[] options) {
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setPreferredSize(new Dimension(200, 40)); // Set combo box size
        comboBox.setBackground(Color.YELLOW);
        comboBox.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 18));
        comboBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        return comboBox;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataVisualizationGUI gui = new DataVisualizationGUI();
            gui.setVisible(true);
        });
    }
}

