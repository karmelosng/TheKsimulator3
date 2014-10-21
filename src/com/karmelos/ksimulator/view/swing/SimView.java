package com.karmelos.ksimulator.view.swing;

//package com.karmelos.ksimulator.view.swing;
import com.badlogic.gdx.assets.AssetManager;
import com.karmelos.ksimulator.controller.SimController;
import com.karmelos.ksimulator.exception.SimException;
import com.karmelos.ksimulator.jdialogs.CancelOption;
import com.karmelos.ksimulator.jdialogs.OkCancelOption;
import com.karmelos.ksimulator.jdialogs.OkOption;
import com.karmelos.ksimulator.jdialogs.SaveDialog;
import com.karmelos.ksimulator.jdialogs.YesNoOption;
import com.karmelos.ksimulator.model.SimComponent;
import com.karmelos.ksimulator.model.SimModule;
import com.karmelos.ksimulator.model.SimModuleType;
import com.karmelos.ksimulator.model.SimPoint;
import com.karmelos.ksimulator.model.SimState;
import com.karmelos.ksimulator.model.SimUser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.j3d.BranchGroup;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Fatimehin Dare
 */
public class SimView extends javax.swing.JFrame implements Observer {

    private static final long serialVersionUID = -5331262611611474941L;
    private static final Logger LOGGER = Logger.getLogger(SimView.class.getName());
    private SimController controller;
    private JTextField userName;
    private AssetManager assets;
    private JPasswordField password;
    private PopUpFrame popupFrame;//JFrame to add panels for pop up menus        
    private PrinterPanel printerPanel; //Print WorkSpace&3Dview option panel
    private HelpTipsPanel helpPanel;// Help Tips jpanel
    private DemoVideoPanel demoPanel;//Demo video jpanel
    private AboutPanel aboutPanel;//About jpanel
    private SavePanel savePanel;
    private LoginPane loginPanel;
    private StatePanel openPanel;
    private LicensePanel licensePanel;
    private DefaultListModel defaultListModelAvailable;
    private DefaultListModel defaultListModelUsed;
    private SimUser presentSimUser;
    private ImageIcon lockedIcon;
    private ImageIcon unlockedIcon;
    private ImageIcon themeIndicator;
    private boolean rerender;
    private boolean onSaveClear;
    private GridPanel droppablePanel;
    private ComponentDrag componentDrag;
    private SimComponent highlightedComponent;
    private ButtonGroup themeButtonGroup;
    List<JLabel> listOfLabels;
    private int moduleIndexselected;
    private static SimView sv;
    private ChangeUserDialog cuDialog;
    private String display;
    private long startTime;
    private boolean start;
    private static final SimpleDateFormat sdm = new SimpleDateFormat("mm:ss");
    private Thread thread;
    private int dropTimes;
    private long savedTime;
    private long droppingTime;
    private double howLong;
    private double score;
    private String scores;
    private long stopTime;
    private boolean canSave;
    private List<SimComponent> placedList = new ArrayList<>();
    private int level = 9;
    private Runnable runner = new Runnable() {

        @Override
        public void run() {
            timer();
        }
    };

    public javax.swing.JLabel getYesnoLabel() {
        return yesnoLabel;
    }

    public void setYesnoLabel(javax.swing.JLabel yesnoLabel) {
        this.yesnoLabel = yesnoLabel;
    }

    /**
     * Creates new form SimView
     */
    public SimView() throws IOException {

        //instantiate Persistence and SimController
        controller = new SimController();
        // addObserver to the initll 
        controller.getDummyState().addObserver(this);
        controller.getDummyState().notifyObservers();

        //check Server Availability Thread is started here
        try {
            UIManager.setLookAndFeel(controller.getPresentTheme());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
        lockedIcon = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/locked24.png"));
        unlockedIcon = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/unlocked24.png"));
        themeIndicator = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/helpsmall.png"));
        themeButtonGroup = new ButtonGroup();
        listOfLabels = new ArrayList<>();
        defaultListModelAvailable = new DefaultListModel();
        defaultListModelUsed = new DefaultListModel();
        onSaveClear = false;

//        try {
//            UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
//        } catch (UnsupportedLookAndFeelException ex) {
//            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //instantiate Jframe to add pop up panels
        popupFrame = new PopUpFrame();

        //instantiate panels
        printerPanel = new PrinterPanel();
        helpPanel = new HelpTipsPanel();
        aboutPanel = new AboutPanel();
        demoPanel = new DemoVideoPanel();
        savePanel = new SavePanel();
        loginPanel = new LoginPane();
        openPanel = new StatePanel();
        licensePanel = new LicensePanel();

        //create GridPanel where Component Images would be drawn
        droppablePanel = new GridPanel();

        //check and retrieve user Component HERE
        setTitle("KSimulator");
        initComponents();
//        loadingProgressBar.setVisible(false);
        //instantatiate Themes as CheckBoxes of themes and Add to ThemeMenu
        //LoadAllAvailableThemes(); 
        init();
        timerLabel.setText("00:00");
        canSave = true;
        //Add Listeners to the Generated VectorList of Checboxes
//        clock.start();
        AttachListener(listOfLabels);
    }

    public JLabel getSessionDescLabel() {
        return sessionDescLabel;
    }

    public void setSessionDescLabel(JLabel sessionDescLabel) {
        this.sessionDescLabel = sessionDescLabel;
    }

    public JLabel getLoggedLabel() {
        return LoggedLabel;
    }

    public void setLoggedLabel(JLabel LoggedLabel) {
        this.LoggedLabel = LoggedLabel;
    }

    public Font fontCreate(String fName, int size) {
        String fullPath = "fonts/" + fName;
        Font customFont = null;
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fullPath)).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fullPath)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

        return customFont;

    }

    public void timer() {
        while (start) {
            timerLabel.setText(sdm.format(new Date((System.currentTimeMillis() - startTime) + savedTime)));
        }
//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (dropTimes>1)
//            {start = false;
//                System.out.println(dropTimes);
//            }
    }

    public void AttachCheckBoxes(JMenu menutheme) {
        try {

            Map<String, String> control = controller.retrieveThemeSettings();
            // get keySet Alone           
            Iterator<String> keySet = control.keySet().iterator();
            String currentTheme = control.get("currenttheme");
            while (keySet.hasNext()) {
                String keyed = keySet.next();
                if (!keyed.equalsIgnoreCase("currenttheme")) {
                    JLabel jcheck = new JLabel(keyed);
                    listOfLabels.add(jcheck);
                    if (currentTheme.equals(control.get(keyed))) {
                        jcheck.setIcon(themeIndicator);

                    }
                    menutheme.add(jcheck);
                }
                //check which is currentTheme and put marker on

            }// end while
            controller.setListofAttachedThemes(listOfLabels);

        } catch (SQLException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void AttachListener(List<JLabel> vecList) {

        for (JLabel j : vecList) {

            j.addMouseListener(new ThemeChangeListener(controller.getMapOfThemes(), controller, vecList));

        }

    }

    public void reloadListsOnOpen(List<SimComponent> availableOnOpen, Map<SimComponent, SimPoint> usedOnOpen) {   // iterate for available    
        // reload for available components
        defaultListModelAvailable.removeAllElements();
        defaultListModelUsed.removeAllElements();
        for (int i = 0; i < availableOnOpen.size(); i++) {
            defaultListModelAvailable.addElement(availableOnOpen.get(i));
        }

        // reload for used component
        for (SimComponent simComp : usedOnOpen.keySet()) {
            defaultListModelUsed.addElement(simComp);

        }

        //free the Disabled Submenus
        saveItem.setEnabled(true);
        printItem.setEnabled(true);

        undoSubMenu.setEnabled(true);

        controller.setDropOccured(true);

        droppablePanel.repaint();
    }

    public boolean isOnSaveClear() {
        return onSaveClear;
    }

    public void setOnSaveClear(boolean onSaveClear) {
        this.onSaveClear = onSaveClear;
    }

    public SimController getController() {
        return controller;
    }

    public void setController(SimController controller) {
        this.controller = controller;
    }

    private DefaultListModel getDefaultListModelAvailable() {
        return defaultListModelAvailable;
    }

    public void setDefaultListModelAvailable(DefaultListModel defaultListModelAvailable) {
        this.defaultListModelAvailable = defaultListModelAvailable;
    }

    public DefaultListModel getDefaultListModelUsed() {
        return defaultListModelUsed;
    }

    public void setDefaultListModelUsed(DefaultListModel defaultListModelUsed) {
        this.defaultListModelUsed = defaultListModelUsed;
    }

    public int getModuleIndexselected() {
        return moduleIndexselected;
    }

    public void setModuleIndexselected(int moduleIndexselected) {
        this.moduleIndexselected = moduleIndexselected;
    }

    public void reloadLoginImage(boolean access) {
        if (access) {
            logiconLabel.setIcon(unlockedIcon);
        } else {
            logiconLabel.setIcon(lockedIcon);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        descriptionDialog = new javax.swing.JDialog();
        sessionName = new javax.swing.JLabel();
        descDialogTxt = new javax.swing.JTextField();
        descripDialogBtn = new javax.swing.JButton();
        noDescTypedError = new javax.swing.JLabel();
        CorrectlyPlacedDialog = new javax.swing.JDialog();
        saveDialog = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        saveDialogsave = new javax.swing.JButton();
        saveDialogdont = new javax.swing.JButton();
        saveDialogcancel = new javax.swing.JButton();
        yesnocancel = new javax.swing.JDialog();
        yesnoLabel = new javax.swing.JLabel();
        yesnoyes = new javax.swing.JButton();
        yesnono = new javax.swing.JButton();
        yenocancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        updatechecked = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        LoggedLabel = new javax.swing.JLabel();
        logiconLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        newSubMenu = new javax.swing.JLabel();
        printSubMenu = new javax.swing.JLabel();
        redoSubMenu = new javax.swing.JLabel();
        clearWorkSpaceSubMenu = new javax.swing.JLabel();
        open = new javax.swing.JLabel();
        saveShortcut = new javax.swing.JLabel();
        infoShortcut = new javax.swing.JLabel();
        undoSubMenu = new javax.swing.JLabel();
        sessionDescLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        serverNotification = new javax.swing.JLabel();
        sessionStaticText = new javax.swing.JLabel();
        componentListPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        availableCompList = new javax.swing.JList();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        usedComponentsList = new javax.swing.JList();
        deleteOption = new javax.swing.JPanel();
        removeUsed = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        moduleCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        selectModuleTypeCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        simulationPanel = new javax.swing.JPanel();
        workSpacePanel = new javax.swing.JPanel();
        status = new javax.swing.JLabel();
        sessionDescritionLabel = new javax.swing.JLabel();
        updateStatus = new javax.swing.JLabel();
        timerLabel = new javax.swing.JLabel();
        timer = new javax.swing.JCheckBox();
        scoreText = new javax.swing.JLabel();
        scoreLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        levelCombo = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newItem = new javax.swing.JMenuItem();
        openItem = new javax.swing.JMenuItem();
        saveItem = new javax.swing.JMenuItem();
        exitItem = new javax.swing.JMenuItem();
        printItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        recentMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        editMenu = new javax.swing.JMenu();
        undoItem = new javax.swing.JMenuItem();
        redoItem = new javax.swing.JMenuItem();
        themeMenu = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        viewMenu = new javax.swing.JMenu();
        workCheckBox = new javax.swing.JCheckBoxMenuItem();
        edCheckBox = new javax.swing.JCheckBoxMenuItem();
        jMenu6 = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        tipsItem = new javax.swing.JMenuItem();
        videoItem = new javax.swing.JMenuItem();
        licenseItem = new javax.swing.JMenuItem();
        aboutItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();

        descriptionDialog.getRootPane().setDefaultButton(descripDialogBtn);
        descriptionDialog.setTitle("KSimulator");
        descriptionDialog.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        descriptionDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        sessionName.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        sessionName.setText("Session Name:");

        descDialogTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                descDialogTxtMouseClicked(evt);
            }
        });

        descripDialogBtn.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        descripDialogBtn.setText("Ok");
        descripDialogBtn.requestFocus();
        descripDialogBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descripDialogBtnActionPerformed(evt);
            }
        });

        noDescTypedError.setForeground(new java.awt.Color(255, 51, 0));

        javax.swing.GroupLayout descriptionDialogLayout = new javax.swing.GroupLayout(descriptionDialog.getContentPane());
        descriptionDialog.getContentPane().setLayout(descriptionDialogLayout);
        descriptionDialogLayout.setHorizontalGroup(
            descriptionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(descriptionDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sessionName)
                .addGap(13, 13, 13)
                .addComponent(descDialogTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(descripDialogBtn)
                .addContainerGap())
            .addGroup(descriptionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(descriptionDialogLayout.createSequentialGroup()
                    .addGap(0, 192, Short.MAX_VALUE)
                    .addComponent(noDescTypedError)
                    .addGap(0, 192, Short.MAX_VALUE)))
        );
        descriptionDialogLayout.setVerticalGroup(
            descriptionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(descriptionDialogLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(descriptionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sessionName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descripDialogBtn)
                    .addComponent(descDialogTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(descriptionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(descriptionDialogLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(noDescTypedError)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout CorrectlyPlacedDialogLayout = new javax.swing.GroupLayout(CorrectlyPlacedDialog.getContentPane());
        CorrectlyPlacedDialog.getContentPane().setLayout(CorrectlyPlacedDialogLayout);
        CorrectlyPlacedDialogLayout.setHorizontalGroup(
            CorrectlyPlacedDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        CorrectlyPlacedDialogLayout.setVerticalGroup(
            CorrectlyPlacedDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        saveDialog.setTitle("KSimulator");
        saveDialog.setIconImage(getIconImage());

        jLabel3.setText("Do you want to save changes to simulation?");

        saveDialogsave.setText("     Save   ");

        saveDialogdont.setText("Don't Save");

        saveDialogcancel.setText("   Cancel  ");

        javax.swing.GroupLayout saveDialogLayout = new javax.swing.GroupLayout(saveDialog.getContentPane());
        saveDialog.getContentPane().setLayout(saveDialogLayout);
        saveDialogLayout.setHorizontalGroup(
            saveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveDialogLayout.createSequentialGroup()
                .addGroup(saveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(saveDialogLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(saveDialogsave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveDialogdont)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveDialogcancel))
                    .addGroup(saveDialogLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel3)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        saveDialogLayout.setVerticalGroup(
            saveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveDialogLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(saveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveDialogsave)
                    .addComponent(saveDialogdont)
                    .addComponent(saveDialogcancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        yesnocancel.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        yesnocancel.setTitle("KSimulator");
        yesnocancel.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        yesnocancel.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        yesnoLabel.setText("jLabel9");

        yesnoyes.setText("  Yes ");

        yesnono.setText("   No ");

        yenocancel.setText("Cancel");

        javax.swing.GroupLayout yesnocancelLayout = new javax.swing.GroupLayout(yesnocancel.getContentPane());
        yesnocancel.getContentPane().setLayout(yesnocancelLayout);
        yesnocancelLayout.setHorizontalGroup(
            yesnocancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(yesnocancelLayout.createSequentialGroup()
                .addGroup(yesnocancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(yesnocancelLayout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(yesnoLabel))
                    .addGroup(yesnocancelLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(yesnoyes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(yesnono)))
                .addGap(57, 57, 57)
                .addComponent(yenocancel)
                .addContainerGap(74, Short.MAX_VALUE))
        );
        yesnocancelLayout.setVerticalGroup(
            yesnocancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(yesnocancelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(yesnoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(yesnocancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yesnoyes)
                    .addComponent(yesnono)
                    .addComponent(yenocancel))
                .addGap(20, 20, 20))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("KSimulator");
        setBackground(new java.awt.Color(101, 106, 114));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        jLabel5.setText("Present User");

        LoggedLabel.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        LoggedLabel.setForeground(new java.awt.Color(51, 51, 255));
        LoggedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/downarrow.png"))); // NOI18N
        LoggedLabel.setToolTipText("Click to change User");
        LoggedLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LoggedLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        LoggedLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoggedLabelMouseClicked(evt);
            }
        });

        logiconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/locked24.png"))); // NOI18N
        logiconLabel.setToolTipText("Login Status");

        newSubMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/newFile24.png"))); // NOI18N
        newSubMenu.setToolTipText("Start New Session");
        newSubMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newSubMenuMouseClicked(evt);
            }
        });

        printSubMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/print24.png"))); // NOI18N
        printSubMenu.setToolTipText("Print");
        printSubMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printSubMenuMouseClicked(evt);
            }
        });

        redoSubMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/redo24.png"))); // NOI18N
        redoSubMenu.setToolTipText("Redo");
        redoSubMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                redoSubMenuMouseClicked(evt);
            }
        });

        clearWorkSpaceSubMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/cleanCurrentProject24.gif"))); // NOI18N
        clearWorkSpaceSubMenu.setToolTipText("Clear Workspace");
        clearWorkSpaceSubMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearWorkSpaceSubMenuMouseClicked(evt);
            }
        });

        open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/openProject24.png"))); // NOI18N
        open.setToolTipText("Open Saved Session");
        open.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openMouseClicked(evt);
            }
        });

        saveShortcut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/save_results24.png"))); // NOI18N
        saveShortcut.setToolTipText("Save Session");
        saveShortcut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveShortcutMouseClicked(evt);
            }
        });

        infoShortcut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/icon2.png"))); // NOI18N
        infoShortcut.setToolTipText("Info");
        infoShortcut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                infoShortcutMouseClicked(evt);
            }
        });

        undoSubMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/undo24.png"))); // NOI18N
        undoSubMenu.setToolTipText("Undo");
        undoSubMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                undoSubMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(newSubMenu)
                .addGap(18, 18, 18)
                .addComponent(saveShortcut)
                .addGap(18, 18, 18)
                .addComponent(open)
                .addGap(18, 18, 18)
                .addComponent(undoSubMenu)
                .addGap(18, 18, 18)
                .addComponent(redoSubMenu)
                .addGap(18, 18, 18)
                .addComponent(clearWorkSpaceSubMenu)
                .addGap(18, 18, 18)
                .addComponent(printSubMenu)
                .addGap(18, 18, 18)
                .addComponent(infoShortcut)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(undoSubMenu)
                    .addComponent(infoShortcut)
                    .addComponent(saveShortcut)
                    .addComponent(open)
                    .addComponent(redoSubMenu)
                    .addComponent(clearWorkSpaceSubMenu)
                    .addComponent(printSubMenu)
                    .addComponent(newSubMenu)))
        );

        sessionDescLabel.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        sessionDescLabel.setForeground(new java.awt.Color(255, 51, 51));
        sessionDescLabel.setText("None");
        sessionDescLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sessionDescLabelMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Server Status:");

        serverNotification.setFont(sessionDescLabel.getFont());
        serverNotification.setForeground(new java.awt.Color(204, 0, 0));

        sessionStaticText.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        sessionStaticText.setText("Session:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(763, 763, 763)
                        .addComponent(updatechecked)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 469, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(177, 177, 177)
                        .addComponent(sessionStaticText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sessionDescLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(serverNotification, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logiconLabel)
                        .addGap(6, 6, 6)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(LoggedLabel)
                        .addGap(86, 86, 86))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LoggedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 18, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(serverNotification, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(sessionStaticText)
                                                    .addComponent(sessionDescLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel7)))))
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(logiconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(updatechecked)
                .addContainerGap())
        );

        componentListPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        componentListPanel.setPreferredSize(new java.awt.Dimension(200, 578));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Available Components", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Trebuchet MS", 1, 12))); // NOI18N
        jPanel9.setFont(new java.awt.Font("Trebuchet MS", 0, 10)); // NOI18N

        availableCompList.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        availableCompList.setModel(defaultListModelAvailable);
        availableCompList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        availableCompList.setCellRenderer(new IconListRenderer());
        availableCompList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        availableCompList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                availableCompListMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                availableCompListMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                availableCompListMousePressed(evt);
            }
        });
        availableCompList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                availableCompListKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(availableCompList);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Used Components", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Trebuchet MS", 1, 11))); // NOI18N

        usedComponentsList.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        usedComponentsList.setModel(defaultListModelUsed);
        usedComponentsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        usedComponentsList.setCellRenderer(new IconListRenderer());
        usedComponentsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usedComponentsListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(usedComponentsList);

        deleteOption.setToolTipText("Click to Delete Component, Wont Work if no Component is Selected");
        deleteOption.setEnabled(false);
        deleteOption.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteOptionMouseClicked(evt);
            }
        });

        removeUsed.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        removeUsed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/cleanProject24.gif"))); // NOI18N
        removeUsed.setText("<HTML><U>Remove Component</U></HTML>");
        removeUsed.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        removeUsed.setIconTextGap(2);

        javax.swing.GroupLayout deleteOptionLayout = new javax.swing.GroupLayout(deleteOption);
        deleteOption.setLayout(deleteOptionLayout);
        deleteOptionLayout.setHorizontalGroup(
            deleteOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteOptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(removeUsed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        deleteOptionLayout.setVerticalGroup(
            deleteOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteOptionLayout.createSequentialGroup()
                .addComponent(removeUsed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(0, 63, Short.MAX_VALUE)
                        .addComponent(deleteOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(deleteOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        moduleCombo.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        moduleCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Type" }));
        moduleCombo.setEnabled(false);
        moduleCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                moduleComboItemStateChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel2.setText("         Select  Module:");

        selectModuleTypeCombo.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        selectModuleTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Module Type" }));
        selectModuleTypeCombo.setEnabled(false);
        selectModuleTypeCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selectModuleTypeComboItemStateChanged(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        jLabel4.setText("Select Module Type:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(moduleCombo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(selectModuleTypeCombo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(1, 1, 1)
                .addComponent(selectModuleTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(1, 1, 1)
                .addComponent(moduleCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout componentListPanelLayout = new javax.swing.GroupLayout(componentListPanel);
        componentListPanel.setLayout(componentListPanelLayout);
        componentListPanelLayout.setHorizontalGroup(
            componentListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(componentListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(componentListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        componentListPanelLayout.setVerticalGroup(
            componentListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(componentListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        simulationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        simulationPanel.setForeground(new java.awt.Color(101, 106, 114));
        simulationPanel.setPreferredSize(new java.awt.Dimension(1130, 578));

        workSpacePanel.setBackground(new java.awt.Color(255, 255, 255));
        workSpacePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "WorkSpace", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Trebuchet MS", 1, 14), new java.awt.Color(0, 0, 0)));
        workSpacePanel.add(droppablePanel);
        workSpacePanel.setPreferredSize(new java.awt.Dimension(550, 552));
        workSpacePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                workSpacePanelMouseEntered(evt);
            }
        });
        workSpacePanel.setLayout(new java.awt.GridLayout(1, 0));

        status.setFont(new java.awt.Font("Trebuchet MS", 1, 11)); // NOI18N
        status.setText("STATUS:");

        updateStatus.setBackground(new java.awt.Color(255, 255, 255));
        updateStatus.setFont(sessionStaticText.getFont());
        updateStatus.setForeground(new java.awt.Color(255, 0, 0));

        timerLabel.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        timerLabel.setText("00:00");
        timerLabel.setToolTipText("Count timer");
        timerLabel.setEnabled(false);

        timer.setFont(status.getFont());
        timer.setText("Timer:");
        timer.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        timer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        timer.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timerStateChanged(evt);
            }
        });

        scoreText.setFont(status.getFont());
        scoreText.setText("Score:");

        scoreLabel.setFont(timerLabel.getFont());
        scoreLabel.setText("0");
        scoreLabel.setToolTipText("Your score");
        scoreLabel.setEnabled(false);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        levelCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Beginner", "Intermidiate", "Expert" }));
        levelCombo.setSelectedItem("Intermidiate");
        levelCombo.setToolTipText("Select your expert level"); // NOI18N
        levelCombo.setEnabled(false);
        levelCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                levelComboItemStateChanged(evt);
            }
        });

        jLabel6.setFont(status.getFont());
        jLabel6.setText("Level:");

        javax.swing.GroupLayout simulationPanelLayout = new javax.swing.GroupLayout(simulationPanel);
        simulationPanel.setLayout(simulationPanelLayout);
        simulationPanelLayout.setHorizontalGroup(
            simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simulationPanelLayout.createSequentialGroup()
                .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(simulationPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(workSpacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, simulationPanelLayout.createSequentialGroup()
                        .addComponent(sessionDescritionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(271, 271, 271)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(levelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(scoreText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scoreLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timer)
                        .addGap(4, 4, 4)
                        .addComponent(timerLabel)
                        .addGap(66, 66, 66)
                        .addComponent(status)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        simulationPanelLayout.setVerticalGroup(
            simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simulationPanelLayout.createSequentialGroup()
                .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(simulationPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sessionDescritionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scoreLabel)
                            .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(scoreText)
                                .addComponent(levelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))
                            .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(updateStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(status)
                                    .addComponent(timerLabel)
                                    .addComponent(timer))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(workSpacePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        menuBar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        fileMenu.setText("File");
        fileMenu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        newItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        newItem.setText("New");
        newItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newItemActionPerformed(evt);
            }
        });
        fileMenu.add(newItem);

        openItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        openItem.setText("Open");
        openItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openItemActionPerformed(evt);
            }
        });
        fileMenu.add(openItem);

        saveItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        saveItem.setText("Save");
        saveItem.setEnabled(false);
        saveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveItem);

        exitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exitItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        exitItem.setText("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);

        printItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        printItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        printItem.setText("Print");
        printItem.setEnabled(false);
        printItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printItemActionPerformed(evt);
            }
        });
        fileMenu.add(printItem);
        fileMenu.add(jSeparator1);

        recentMenu.setText("Recent");
        recentMenu.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        recentMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                recentMenuMenuSelected(evt);
            }
        });
        recentMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recentMenuActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Recently Added");
        jMenuItem1.setEnabled(false);
        recentMenu.add(jMenuItem1);

        fileMenu.add(recentMenu);

        menuBar.add(fileMenu);

        jMenu2.setEnabled(false);
        jMenu2.setPreferredSize(new java.awt.Dimension(20, 19));
        menuBar.add(jMenu2);

        editMenu.setText("Edit");
        editMenu.setFont(fileMenu.getFont());

        undoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        undoItem.setText("Undo");
        undoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoItemActionPerformed(evt);
            }
        });
        editMenu.add(undoItem);

        redoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redoItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        redoItem.setText("Redo");
        redoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoItemActionPerformed(evt);
            }
        });
        editMenu.add(redoItem);

        themeMenu.setText("Themes");
        themeMenu.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        editMenu.add(themeMenu);

        menuBar.add(editMenu);

        jMenu4.setEnabled(false);
        jMenu4.setPreferredSize(new java.awt.Dimension(20, 19));
        menuBar.add(jMenu4);

        viewMenu.setText("View");
        viewMenu.setFont(fileMenu.getFont());

        buttonGroup1.add(workCheckBox);
        workCheckBox.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        workCheckBox.setSelected(true);
        workCheckBox.setText("Work-View");
        viewMenu.add(workCheckBox);

        buttonGroup1.add(edCheckBox);
        edCheckBox.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        edCheckBox.setText("3D-View");
        edCheckBox.setEnabled(false);
        edCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edCheckBoxActionPerformed(evt);
            }
        });
        viewMenu.add(edCheckBox);

        menuBar.add(viewMenu);

        jMenu6.setEnabled(false);
        jMenu6.setPreferredSize(new java.awt.Dimension(20, 19));
        menuBar.add(jMenu6);

        helpMenu.setText("Help");
        helpMenu.setFont(fileMenu.getFont());

        tipsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        tipsItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        tipsItem.setText("Help Tips");
        tipsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipsItemActionPerformed(evt);
            }
        });
        helpMenu.add(tipsItem);

        videoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK));
        videoItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        videoItem.setText("Video");
        videoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoItemActionPerformed(evt);
            }
        });
        helpMenu.add(videoItem);

        licenseItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_MASK));
        licenseItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        licenseItem.setText("License And Agreement");
        licenseItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        licenseItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                licenseItemActionPerformed(evt);
            }
        });
        helpMenu.add(licenseItem);

        aboutItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK));
        aboutItem.setFont(new java.awt.Font("Trebuchet MS", 0, 13)); // NOI18N
        aboutItem.setText("About     ");
        aboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        jMenu1.setEnabled(false);
        jMenu1.setPreferredSize(new java.awt.Dimension(20, 19));
        menuBar.add(jMenu1);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(componentListPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(simulationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1059, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(componentListPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(simulationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //action performed when the Print menu item on the top menu panel is clicked
    private void printItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printItemActionPerformed

        popupFrame.addPanels(printerPanel);// invokes AddPanel(Jpanel pane) method from PopUpFrame
        printerPanel.setBorder(new EmptyBorder(30, 60, 0, 0));
        droppablePanel.repaint();
        printerPanel.setSize(400, 350);
        popupFrame.setSize(printerPanel.getSize());
        popupFrame.setVisible(true);//displays frame

        //set Icon for Pop Up frame
        try {
            popupFrame.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
        } catch (IOException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
        //set PopUpFrame title
        popupFrame.setTitle("KSimulator");

    }//GEN-LAST:event_printItemActionPerformed

    //action performed when the Open menu item is clicked
    private void openItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openItemActionPerformed

        if (controller.getDropOccured()) {
            System.out.println("Drop just occurred");
//            Add Save panel and then Open new
//            try {
//                controller.getState().setSimUser(getPresentSimUser());
//                controller.setInstigClear(true);
//                PopUpFrame puf = popupFrame.addSavePanel(savePanel, this);
//                puf.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
//                savePanel.setSize(371, 300);
//                puf.setSize(savePanel.getSize());
//                puf.setVisible(true);
//            } catch (IOException ex) {
//                Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            PopUp the openPanel After clearing all

        }
        if (getPresentSimUser() != null && controller.getState() == null) {
//            try {
            System.out.println("user not null no simulation started");
            startSimulation();
//                PopUpFrame temp = popupFrame.addOpenPanel(openPanel, this);
//
////                temp.setTitle("Open Saved Sessions");
//                temp.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
//                temp.setVisible(true);
//                openPanel.setSize(300, 150);
//                temp.setTitle("KSimulator");
//                
////                temp.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
//                //temp.setSize(openPanel.getSize());
//                openPanel.refresh();
//                temp.setVisible(true);
//            } catch (IOException ex) {
//                Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
            openOpen();
//            }
        } else if ((getPresentSimUser() == null && controller.getState() == null) || (getPresentSimUser() == null && controller.getState() != null)) {
            System.out.println("user null cann t open");
            OkOption ok = new OkOption(this, "KSimulator");
            ok.setLabel1("Guest users cannot open a session");
            ok.setSize(350, 100);
            ok.showDialog();

        } else if (getPresentSimUser() != null && controller.getState() != null && controller.isSaved()) {

            OkCancelOption okcancel = new OkCancelOption(this, "KSimulator");
            okcancel.setLabel1("You are about to open a saved session, your current session will be cleared. Do you want to continue?");
            okcancel.setSize(650, 100);
            boolean response = okcancel.showDialog();
            if (response) {
                openOpen();

            }

        } else if (getPresentSimUser() != null && controller.getState() != null && !controller.isSaved()) {
            OkOption yesno = new OkOption(this, "KSimulator");
            yesno.setLabel1("Please save your current session");
            yesno.setSize(250, 100);
            yesno.showDialog();
            opensave();

        } else {
            OkOption not = new OkOption(this, "KSimulator");
            not.setLabel1("Situation not anticipated");
            not.setSize(200, 100);
            not.showDialog();
        }
        //open the file 
        // open();

    }//GEN-LAST:event_openItemActionPerformed
//actionperormed when exit menu item is clicked

    private void opensave() {
        try {
            PopUpFrame puff = popupFrame.addSavePanel(savePanel, this);
            puff.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
            savePanel.setSize(371, 300);
            puff.setTitle("KSimulator");
            puff.setSize(savePanel.getSize());
            puff.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openOpen() {
        try {
            PopUpFrame pop = popupFrame.addOpenPanel(openPanel, this);
            pop.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
            openPanel.setSize(400, 400);
            pop.setTitle("KSimulator");
            new Thread(new Runnable() {

                @Override
                public void run() {
                    openPanel.refresh();
                    openPanel.repaint();
                }
            }).start();
            pop.setSize(openPanel.getSize());
            pop.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
        formWindowClosing(null);
    }//GEN-LAST:event_exitItemActionPerformed

    private void redoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoItemActionPerformed
        SimComponent redoAction = controller.redoAction();

    }//GEN-LAST:event_redoItemActionPerformed

    private void tipsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipsItemActionPerformed
        // Help Menu Item clicked

        popupFrame.addPanels(helpPanel);// invokes AddPanel(Jpanel pane) method from PopUpFrame

        popupFrame.setVisible(true);//displays frame

        //sets  PopUpFrames icon and title
        try {
            popupFrame.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
        } catch (IOException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
        popupFrame.setTitle("Help Tips");

        droppablePanel.repaint();
    }//GEN-LAST:event_tipsItemActionPerformed

    private void videoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoItemActionPerformed
        popupFrame.addPanels(demoPanel);// invokes AddPanel(Jpanel pane) method from PopUpFrame

        popupFrame.setVisible(true);//displays frame

        //sets  PopUpFrames icon and title
        try {
            popupFrame.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
        } catch (IOException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
        popupFrame.setTitle("KSimulator");

        droppablePanel.repaint();


    }//GEN-LAST:event_videoItemActionPerformed

    private void aboutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutItemActionPerformed
        popupFrame.addPanels(aboutPanel);// invokes AddPanel(Jpanel pane) method from PopUpFrame
        popupFrame.setSize(420, 490);
        popupFrame.setVisible(true);//displays frame
        try {
            popupFrame.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
        } catch (IOException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
        popupFrame.setTitle("KSimulator");
        //pack();

        droppablePanel.repaint();

    }//GEN-LAST:event_aboutItemActionPerformed

    private void edCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edCheckBoxActionPerformed
//    public Map<SimComponent,BranchGroup> SceneLize(List<SimComponent> listed){
//        Map<SimComponent, BranchGroup> mapScenes=new HashMap<SimComponent, BranchGroup>();
//           double creaseAngle = 60.0;
//         ObjectFile f = new ObjectFile(0,
//                 (float) (creaseAngle * Math.PI / 180.0));
//    for (SimComponent sc :listed) {
//             
//             Scene scene = null;
//          
//             try {
//                 scene = f.load("KSim3DResource\\obj_" + sc.getId() + ".obj");
//                 mapScenes.put(sc, scene.getSceneGroup());
//                
//             } catch (ParsingErrorException e) {
//                Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, e);
//                 System.exit(1);
//             } catch (IncorrectFormatException e) {
//                 Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, e);
//                 System.exit(1);
//             } catch (FileNotFoundException ex) { 
//                Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
//               } 
//
//         } // end For Scene Loop
//       // Put on SeperateThread
//        availableCompList.setEnabled(true);
//        // loadingProgressBar.setVisible(false);
//    return mapScenes;
//    }

    private void moduleComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_moduleComboItemStateChanged

        SimModule tempSimModule;
        JComboBox temp = (JComboBox) evt.getSource();
        int indexselected = temp.getSelectedIndex();
        List<SimComponent> components = new ArrayList<SimComponent>();
        // once completePhone3D simulation has started no way for the selection of

        if (evt.getStateChange() == ItemEvent.SELECTED) {

            if (!controller.getDropOccured()) {
                if (indexselected > 0) {
                    tempSimModule = controller.fetchModule((long) indexselected);
                    controller.updateStatustext(tempSimModule.getModelName() + " Assembly");
                    display = tempSimModule.getModelName() + " Assembly";

                    if (tempSimModule.getComponents().size() > 0 && controller.getState() != null) {

                        components = tempSimModule.getComponents();

                        //PreLoading happens 
//                       PreLoadAsync preLoader = new PreLoadAsync(this, controller, components);
//                       preLoader.execute();
                        controller.setClearAction(true);
                        controller.setEmptyPlacedComponent(false);
                        defaultListModelAvailable.clear();
                        defaultListModelUsed.clear();
                        List<Number> list = new ArrayList<>();
                        Random rand = new Random();
                        //add Elements
                        //availableCompList.setEnabled(false);
                        for (int i = 0; i < components.size(); i++) {
                            int gen = rand.nextInt(components.size());
                            while (list.contains(gen)) {
                                gen = rand.nextInt(components.size());
                            }
                            list.add(gen);
//                            defaultListModelAvailable.addElement(components.get(i));
                            defaultListModelAvailable.addElement(components.get(gen));

                        }

                        controller.getState().setAvailableComponents(components);
                        controller.setClearAction(true);

                    }// end innerif
                    else if (tempSimModule.getComponents().size() < 1) {

                        controller.setClearAction(true);
                        controller.setEmptyPlacedComponent(false);
                        defaultListModelAvailable.clear();
                        defaultListModelUsed.clear();

                        controller.updateStatustext("No Components for " + tempSimModule.getModelName());
                        OkOption ok3 = new OkOption(this, "KSimulator");
                        ok3.setLabel1("No Components for " + tempSimModule.getModelName());
                        ok3.setSize(350, 100);
                        ok3.showDialog();
                    }

                }// inner IF 
            } // OuterIF
            else if (controller.getDropOccured() && !controller.isSaved()) {

                moduleCombo.setEnabled(false);
                OkOption ok = new OkOption(this, "KSimulator");
                ok.setLabel1("Simulation has started already!! You can not make module changes until you exit or save");
                ok.setSize(600, 100);
                ok.showDialog();
                moduleCombo.setEnabled(false);
            } else {
                OkOption op = new OkOption(this, "KSimulator");
                op.setLabel1("This situation was not anticipated");
                op.setSize(250, 100);
                op.showDialog();
            }
        }
        if (controller.getSimUser() == null) { // not logged in User
            controller.createUndoRedoFile("guestlogger.klog");
        } else {
            controller.createUndoRedoFile("logger.klog");
        }

    }//GEN-LAST:event_moduleComboItemStateChanged

    private void availableCompListMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_availableCompListMouseEntered
//        JList l = (JList)evt.getSource();
//            ListModel m = l.getModel();
//            int index = l.locationToIndex(evt.getPoint());
//            if( index>-1 ) {
//                l.setToolTipText(m.getElementAt(index).toString());
        //}// TODO add your handling code here:
    }//GEN-LAST:event_availableCompListMouseEntered

    //TODO follow proper naming conventions
    private void availableCompListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_availableCompListMouseClicked
        this.setFocusable(false);
        availableCompList.setFocusable(true);
        if (evt.getClickCount() == 1 && !availableCompList.isSelectionEmpty()) {

            JList l = (JList) evt.getSource();
            int index = l.locationToIndex(evt.getPoint());
            SimComponent temp = (SimComponent) defaultListModelAvailable.getElementAt(index);
            try {
                l.setComponentPopupMenu(new CustomisePopUp(true, temp));

            } catch (IOException ex) {
                Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (evt.getClickCount() == 2) {
//            moduleCombo.setEnabled(false);
//            selectModuleTypeCombo.setEnabled(false);
            JList l = (JList) evt.getSource();
            //ListModel m = l.getModel();get
            int index = l.locationToIndex(evt.getPoint());
            if (index > -1) {
                SimComponent temp = (SimComponent) defaultListModelAvailable.getElementAt(index);
                try {
                    controller.addComponent(temp);
                } catch (SimException ex) {
                    Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  // End innerIF

        }

    }//GEN-LAST:event_availableCompListMouseClicked

    private void usedComponentsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usedComponentsListMouseClicked
        //used componentt mouse right clicked
        if (evt.getClickCount() == 1 && !usedComponentsList.isSelectionEmpty()) {

            JList l = (JList) evt.getSource();
            int index = l.locationToIndex(evt.getPoint());
            SimComponent temp = (SimComponent) defaultListModelUsed.getElementAt(index);
            try {
                l.setComponentPopupMenu(new CustomizeDeletePopup(temp, controller));
                if (!controller.isSaved()) {
                    System.out.println("Not saved state");
                }
            } catch (IOException ex) {
                Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //delete used component
        JList l = (JList) evt.getSource();
        //ListModel m = l.getModel();get

        if (!l.isSelectionEmpty()) {

            deleteOption.setBackground(Color.GREEN);
        }

    }//GEN-LAST:event_usedComponentsListMouseClicked
    private void makeNewSessionDialog() {
        setOnSaveClear(false);
        descriptionDialog.setSize(410, 110);
        descriptionDialog.setTitle("KSimulator");
        descDialogTxt.setText("");
        try {
            descriptionDialog.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
        } catch (IOException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
        descriptionDialog.setLocationRelativeTo(null);
        descriptionDialog.setVisible(true);
        descriptionDialog.getParent().setEnabled(false);

    }

    private void newItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newItemActionPerformed
//        if (!controller.getDropOccured() && controller.getState() == null) {
//            makeNewSessionDialog();
//            
//        }
//        else if(controller.getDropOccured() && controller.getState() == null){
//            System.out.println(" drop and state is null");
//        makeNewSessionDialog();
//        }
//        else if(controller.getSaved() == SimController.SaveState.Saved){
//        OkCancelOption okcancel = new OkCancelOption(this, "KSimulator");
//        okcancel.setLabel1("You are about to start a new session, your current session will be cleared. Do you want to continue?");
//        okcancel.setSize(800, 100);
//        boolean response = okcancel.showDialog();
//        if(response){
//       makeNewSessionDialog();
//       
//        }
//        
//        }
//        else if(controller.getDropOccured() && controller.getState() == null){
//        OkCancelOption okcan = new OkCancelOption(this, "KSimulator"); 
//        okcan.setLabel1("Your current session will not be saved since you are not logged in");
//        okcan.setSize(400, 100);
//        }
//        else {
////            JOptionPane.showMessageDialog(this, "Simulation already Started!! Save to Continue!",
////                    null, JOptionPane.OK_OPTION);
//            System.out.println( "for drop "+controller.getDropOccured());
//            System.out.println("for  state"+controller.getState());
//            OkOption ok_option = new OkOption(this, "KSimulator");
//            ok_option.setMessage("Scenario not anticipated!");
//            ok_option.setSize(300, 100);
//            ok_option.showDialog();
//
//        }

        if (controller.getState() == null) {
            if (controller.getSimUser() == null) {
                makeNewSessionDialog();
            } else if (controller.getSimUser() != null) {
                makeNewSessionDialog();
            }
        } else if (controller.getState() != null) {
            if (controller.getSimUser() == null) {
                YesNoOption yesno = new YesNoOption(this);
                yesno.setLabel1("Your session will not be saved because you are not logged in. Will you like to save this session ?");
                yesno.setSize(620, 100);
                int keyOp = yesno.showDialog();
                switch (keyOp) {
                    case 1:
                        openLogin();
                        break;

                    case 2:
                        makeNewSessionDialog();
                        break;
                    case 3:
                }
            } else if (controller.getSimUser() != null) {
                if (controller.isSaved()) {
                    YesNoOption okcan = new YesNoOption(this);
                    okcan.setLabel1("You are about to start a new session, your current session will be cleared. Do you want to continue ?");
                    okcan.setSize(750, 100);
                    int keyOp = okcan.showDialog();
                    switch (keyOp) {
                        case 1:
                            makeNewSessionDialog();
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }

                } else if (!controller.isSaved()) {
                    YesNoOption yesno = new YesNoOption(this);
                    yesno.setLabel1("Do you want to save this session before opening a new one ?");
                    yesno.setSize(400, 100);
                    int keyOp = yesno.showDialog();
                    switch (keyOp) {
                        case 1:
                            opensave();
                            break;
                        case 2:
                            makeNewSessionDialog();
                            break;
                        case 3:

                    }
                }
            }
        }

    }//GEN-LAST:event_newItemActionPerformed

    private void openLogin() {
        try {
            PopUpFrame popupFrameTemp;

            popupFrameTemp = popupFrame.addLoginPanel(loginPanel, this);
            popupFrameTemp.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
            loginPanel.setSize(380, 400);
            popupFrameTemp.setTitle("KSimulator");
            popupFrameTemp.setSize(loginPanel.getSize());
            popupFrameTemp.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveItemActionPerformed
        //flags on save, if no session started the click save brings an error message else saves session
        PopUpFrame popupFrameTemp;
        if (controller.getState() == null) {
            OkOption ok = new OkOption(this, "KSimulator");
            ok.setLabel1("No simulation started!Click on \"New\" to Start");
            ok.setSize(350, 100);
            ok.showDialog();
        } else {
            if (getPresentSimUser() != null) {
                System.out.println("logged Empty simulation");
                //This COntrols  the save if/when the state placed components is empty
                if (controller.getState().getPlacedComponents().size() < 1) {
                    CancelOption cO = new CancelOption(this, "KSimulator");
                    cO.setLabel1("You cannot save an empty simulation");
                    cO.setSize(300, 100);
                    cO.showDialog();
                } else {
                    try {
                        SimState state = controller.getState();
                        if (timer.isSelected()) {
                            state.setTimed(true);
                            System.out.println("Simulation timed");
                            if (state.getTimerDuration() == null) {
                                state.setTimerDuration(System.currentTimeMillis() - startTime);
                                state.setLevel(levelCombo.getSelectedItem().toString());
                                state.setScore(Double.parseDouble(scores));
                            }
                        }
                        controller.getState().setSimUser(getPresentSimUser());
                        popupFrameTemp = popupFrame.addSavePanel(savePanel, this);
                        savePanel.setSize(371, 300);
                        popupFrameTemp.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
                        popupFrameTemp.setSize(savePanel.getSize());
                        popupFrameTemp.setVisible(true);
                        controller.setSaved(true);
                        System.out.println(controller.isSaved());
                    } catch (IOException ex) {
                        Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else {
//                int response = JOptionPane.showConfirmDialog(this, "This User doesnt Exist! Please Login With your credentials",
//                        null, JOptionPane.OK_CANCEL_OPTION);
//                if (timer.isSelected()) {
//                    long saveTime = System.currentTimeMillis();
//                    controller.getState().setTimerDuration(saveTime - startTime);
//                    controller.getState().setTimed(true);
//                }
                OkCancelOption ok_cancel = new OkCancelOption(this, "KSimulator");
                ok_cancel.setLabel1("This user does not exist! Please login with your credentials");
                ok_cancel.setSize(400, 100);
                boolean response = ok_cancel.showDialog();
                if (response) {
                    popupFrameTemp = popupFrame.addLoginPanel(loginPanel, this);
                    loginPanel.setSize(380, 400);
                    popupFrame.setTitle("KSimulator");
                    popupFrame.setSize(loginPanel.getSize());
                    popupFrameTemp.setVisible(true);
                }
            }

        }

    }//GEN-LAST:event_saveItemActionPerformed

    private void removeUsed(SimComponent sc, ActionEvent evt) {
        sc = (SimComponent) usedComponentsList.getSelectedValue();
//        int index = usedComponentsList.locationToIndex(evt.getPoint());
        if (!usedComponentsList.isSelectionEmpty()) {
//            if (index > -1) {
            // remove Elements from Component3dApplication
            defaultListModelAvailable.addElement(sc);
            defaultListModelUsed.removeElement(sc);

            // remove Elements from model
            controller.getState().getAvailableComponents().add(sc);
            controller.getState().getUsedComponents().remove(sc);
            controller.getState().getPlacedComponents().remove(sc);
            droppablePanel.repaint();
            controller.updateStatustext(sc.getComponentName() + " DELETED!");
            deleteOption.setBackground(null);
//            }
        }
    }

    private void deleteOptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteOptionMouseClicked

        //JList l = (JList) evt.getSource();
        //ListModel m = l.getModel();
        SimComponent sc = (SimComponent) usedComponentsList.getSelectedValue();
        int index = usedComponentsList.locationToIndex(evt.getPoint());
        if (!usedComponentsList.isSelectionEmpty()) {
            if (index > -1) {
                // remove Elements from Component3dApplication
                defaultListModelAvailable.addElement(sc);
                defaultListModelUsed.removeElement(sc);
                // remove Elements from model
                Map<SimComponent, SimPoint> placedComponents = controller.getState().getPlacedComponents();
                // register in undoRedoLog.xml before final delete
                if (controller.getSimUser() == null) { // whe a user isnt logged in
                    controller.createModification("guestlogger.klog", sc.getComponentName(), "GUEST",
                            "deleted", System.currentTimeMillis(), new SimPoint(placedComponents.get(sc).getTopX(), placedComponents.get(sc).getTopY()).toString());
                    controller.splitUndoRedoLog("guestlogger.klog");
                } else {
                    controller.createModification("logger.klog", sc.getComponentName(), controller.getSimUser().getFirstName(),
                            "deleted", System.currentTimeMillis(), new SimPoint(placedComponents.get(sc).getTopX(), placedComponents.get(sc).getTopY()).toString());
                    controller.splitUndoRedoLog("logger.klog");
                }

                controller.getState().getAvailableComponents().add(sc);
                controller.getState().getUsedComponents().remove(sc);
                controller.getState().getPlacedComponents().remove(sc);
                droppablePanel.repaint();
                controller.updateStatustext(sc.getComponentName() + " DELETED!");

//                OkOption ok4 = new OkOption(this, "KSimulator");
//                ok4.setLabel1(sc.getComponentName() + " DELETED!");
//                ok4.showDialog();
                deleteOption.setBackground(null);

            } else {
                deleteOption.setBackground(null);
            }
        } else if ((usedComponentsList.isSelectionEmpty()) && (!defaultListModelUsed.isEmpty())) {
//            JOptionPane.showMessageDialog(this, "Highlight Component To Delete First!",
//                    null, JOptionPane.CANCEL_OPTION);
            CancelOption cancel = new CancelOption(this, "KSimulator");
            cancel.setLabel1("Highlight Component To Delete First!");
            cancel.setSize(300, 100);
            cancel.showDialog();

        } else {
            deleteOption.setBackground(null);
//            JOptionPane.showMessageDialog(this, "No Component in the workspace",
//                    null, JOptionPane.CANCEL_OPTION);
            CancelOption cancel = new CancelOption(this, "KSimulator");
            cancel.setLabel1("No Component in the workspace");
            cancel.setSize(250, 100);
            cancel.showDialog();

        }
    }//GEN-LAST:event_deleteOptionMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        droppablePanel.repaint();
        if (controller.getState() == null || controller.isSaved()) {

            droppablePanel.repaint();
//            int response = JOptionPane.showConfirmDialog(this, "Are You Sure You Want To close Application?",
//                    "Close Ksimulator", JOptionPane.YES_NO_OPTION);
            YesNoOption closing = new YesNoOption(this);
            closing.setLabel1("Are you sure you want to close KSimulator?");
            closing.setSize(300, 100);
            int keyOp = closing.showDialog();
            switch (keyOp) {
                case 1:
                    exit();
                    System.exit(0);
                    break;
                case 2:
                case 3:
            }
        } else if (controller.getState() != null && !controller.isSaved()) {

            droppablePanel.repaint();
//            int response = JOptionPane.showConfirmDialog(this, "Simulation Not saved Yet! Still want to Exit?",
//                    "Close Ksimulator", JOptionPane.YES_NO_OPTION);
//            YesNoOption closing = new YesNoOption(this, "KSimulator");
//            closing.setLabel1("Session not saved yet! Still want to exit?");
//            closing.setSize(350, 100);
//            boolean response = closing.showDialog();

            SaveDialog saveD = new SaveDialog(this);
            saveD.setLabel1("Do you want to save your session?");
            saveD.setSize(330, 100);
            int keyOp = saveD.showDialog();
            switch (keyOp) {
                case 1:
                    saveItemActionPerformed(null);
                    break;
                case 2:
                    System.exit(0);
                    break;
                case 3:

            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void selectModuleTypeComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selectModuleTypeComboItemStateChanged
//loadingProgressBar.setVisible(true);
        // loadingProgressBar.setString("Ready to Load Scenes");
        JComboBox temp = (JComboBox) evt.getSource();
        int indexselected = temp.getSelectedIndex();

        if (controller.getState().getDescription() != null) {
//            if (indexselected == 0) {
//            }
            if (indexselected > 0) {
                SimModule[] simModules = controller.fetchModules((long) indexselected);
                moduleCombo.removeAllItems();
                moduleCombo.setEnabled(true);
                moduleCombo.addItem("Select SubModuleType");
                for (int i = 0; i < simModules.length; i++) {
                    moduleCombo.addItem(simModules[i]);

                }
                timer.setEnabled(false);
                levelCombo.setEnabled(false);
            }

        } else {
//            JOptionPane.showConfirmDialog(this, "Simulation Not saved Yet! Still want to Exit?",
//                    "Close Ksimulator", JOptionPane.YES_NO_OPTION);
            YesNoOption closing = new YesNoOption(this);
            closing.setLabel1("Simulation not saved yet, still want to exit?");
            closing.setSize(350, 100);
            int keyOp = closing.showDialog();
            switch (keyOp) {
                case 1:
                    System.exit(0);
                case 2:
                case 3:

            }
        }

    }//GEN-LAST:event_selectModuleTypeComboItemStateChanged

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

        droppablePanel.repaint();   // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void recentMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recentMenuActionPerformed
    }//GEN-LAST:event_recentMenuActionPerformed

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus

        droppablePanel.repaint();
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowLostFocus

    private void availableCompListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_availableCompListKeyPressed
        // TODO add your handling code here:
        JList l = (JList) evt.getSource();
        SimComponent sc = (SimComponent) l.getSelectedValue();
        if (sc != null) {
            String selected = sc.getComponentName();
            if (selected != null && evt.getKeyCode() == KeyEvent.VK_F1) {
                try {
                    l.setComponentPopupMenu(new CustomisePopUp(false, sc));
                } catch (IOException ex) {
                    Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

                controller.updateStatustext("No Component Selected");
                OkOption ok = new OkOption(this, "KSimulator");
                ok.setLabel1("No Component Selected");
                ok.setSize(200, 100);
                ok.showDialog();

            }

        }
    }//GEN-LAST:event_availableCompListKeyPressed

    private void newSubMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newSubMenuMouseClicked
        newItemActionPerformed(null);        // TODO add your handling code here:
    }//GEN-LAST:event_newSubMenuMouseClicked

    private void printSubMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printSubMenuMouseClicked
        printItemActionPerformed(null);        // TODO add your handling code here:
    }//GEN-LAST:event_printSubMenuMouseClicked

    private void clearWorkSpaceSubMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearWorkSpaceSubMenuMouseClicked
        //clear workspace
        controller.setClearAction(true);
        droppablePanel.repaint();
    }//GEN-LAST:event_clearWorkSpaceSubMenuMouseClicked

    private void openMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openMouseClicked
        // open session
        openItemActionPerformed(null);
    }//GEN-LAST:event_openMouseClicked

    private void saveShortcutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveShortcutMouseClicked
        // save session
        if (!canSave) {
            OkOption ook = new OkOption(this, "KSimulator");
            ook.setLabel1("Simulation is in assessment mode, you can not save until simulation has been completed");
            ook.setSize(520, 95);
            ook.showDialog();
        } else {
            saveItemActionPerformed(null);
        }
    }//GEN-LAST:event_saveShortcutMouseClicked

    private void infoShortcutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_infoShortcutMouseClicked
        aboutItemActionPerformed(null);
    }//GEN-LAST:event_infoShortcutMouseClicked

    private void licenseItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_licenseItemActionPerformed
        // Help Menu Item clicked

        popupFrame.addPanels(licensePanel);// invokes AddPanel(Jpanel pane) method from PopUpFrame
        popupFrame.setVisible(true);//displays frame

        //sets  PopUpFrames icon and title
        try {
            popupFrame.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
        } catch (IOException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
        popupFrame.setTitle("License And Agreement");

        droppablePanel.repaint();
    }//GEN-LAST:event_licenseItemActionPerformed
    private void makeNewSession() {
        if (!descDialogTxt.getText().isEmpty()) {
            if (controller.getState() == null) {
                System.out.println("State is null b4 statrt");
            } else {
                System.out.println("State is not null b4 start");
            }
            startSimulation();

            controller.setLoginClear(false);
            controller.setClearAction(false);
            //setDescription here
            controller.getState().setDescription(descDialogTxt.getText());
            defaultListModelAvailable.clear();
            defaultListModelUsed.clear();
            selectModuleTypeCombo.removeAllItems();
            moduleCombo.removeAllItems();
            moduleCombo.setEnabled(false);
            controller.updateStatustext(null);
            controller.setFirstSave(true);
            start = false;
            if (timer.isSelected()) {
                timerLabel.setText("00:00");
                levelCombo.setEnabled(true);
            }
            timer.setEnabled(true);
            dropTimes = 0;
            thread = null;
//            runner = null;

            // load first combobox
            SimModuleType[] moduleTypes = controller.fetchModuleTypes();

            selectModuleTypeCombo.addItem("Select ModuleType");
            selectModuleTypeCombo.setEnabled(true);
            for (int i = 0; i < moduleTypes.length; i++) {
                selectModuleTypeCombo.addItem(moduleTypes[i]);
            }
            selectModuleTypeCombo.repaint();
            saveItem.setEnabled(true);
            sessionDescLabel.setText(descDialogTxt.getText());

            descriptionDialog.setVisible(false);
            droppablePanel.repaint();

        } else {
            noDescTypedError.setText("You must input session name to continue!");

        }

    }

    private void descripDialogBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descripDialogBtnActionPerformed
        // if no description is typed, then dont remove Dialog
        makeNewSession();

    }//GEN-LAST:event_descripDialogBtnActionPerformed

    private void descDialogTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_descDialogTxtMouseClicked
        noDescTypedError.setText("");    // TODO add your handling code here:
    }//GEN-LAST:event_descDialogTxtMouseClicked

    private void LoggedLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoggedLabelMouseClicked
        // TODO add your handling code here:
        cuDialog = new ChangeUserDialog(this, rerender);
        cuDialog.setController(controller);
        cuDialog.setSimView(this);
//        if(cont)
        cuDialog.showDialog();
    }//GEN-LAST:event_LoggedLabelMouseClicked

    private void availableCompListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_availableCompListMousePressed
        if (moduleCombo.getSelectedIndex() != 0) {
            controller.updateStatustext(display);
        }
    }//GEN-LAST:event_availableCompListMousePressed

    private void sessionDescLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sessionDescLabelMouseClicked
        JOptionPane.showMessageDialog(this, controller.getPreLoadedModel().size());        // TODO add your handling code here:
    }//GEN-LAST:event_sessionDescLabelMouseClicked

    private void recentMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_recentMenuMenuSelected
        //startSimulation();   
        recentMenu.removeAll();
        SimUser usrTemp = new SimUser();
        SimState[] fetchSessions;
        usrTemp.setFirstName(System.getProperty("user.name"));

        if (presentSimUser == null) {
            fetchSessions = controller.getRecentAlone(usrTemp);
            if (fetchSessions.length == 0) {
                recentMenu.add("No Recent Simulation For User");
            } else {
                for (int i = 0; i < fetchSessions.length; i++) {
                    recentMenu.add(fetchSessions[i].toString());
                }
            }
        } else {
            fetchSessions = controller.getRecentAlone(presentSimUser);
            for (int i = 0; i < fetchSessions.length; i++) {
                recentMenu.add(fetchSessions[i].toString());
            }
        }
        recentMenu.getPopupMenu().pack();        // TODO add your handling code here:
    }//GEN-LAST:event_recentMenuMenuSelected

    private void undoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoItemActionPerformed
        if (controller.getState() == null) {
            OkOption okOption = new OkOption(this, "ILLegal Operation");
            okOption.setLabel1("You Cant Do This Operation:\n REASON:: UnRegistered User");
            okOption.setSize(400, 100);
            okOption.showDialog();
        } else {
            String lng = controller.undoAction();
        }

        // JOptionPane.showMessageDialog(this, lng);        // TODO add your handling code here:
    }//GEN-LAST:event_undoItemActionPerformed

    private void undoSubMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_undoSubMenuMouseClicked
        // TODO add your handling code here:
        undoItemActionPerformed(null);
    }//GEN-LAST:event_undoSubMenuMouseClicked

    private void redoSubMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redoSubMenuMouseClicked
        // TODO add your handling code here:
        redoItemActionPerformed(null);
    }//GEN-LAST:event_redoSubMenuMouseClicked

    private void workSpacePanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workSpacePanelMouseEntered

        // TODO add your handling code here:
    }//GEN-LAST:event_workSpacePanelMouseEntered

    private void timerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timerStateChanged
        if (timer.isSelected()) {
            timerLabel.setEnabled(true);
            scoreLabel.setEnabled(true);
            levelCombo.setEnabled(true);
            canSave = false;
        } else if (!timer.isSelected()) {
            timerLabel.setEnabled(false);
            scoreLabel.setEnabled(false);
            levelCombo.setEnabled(false);
            canSave = true;
        }
    }//GEN-LAST:event_timerStateChanged

    private void levelComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_levelComboItemStateChanged
        if (levelCombo.getSelectedIndex() == 0) {
            level = 15;

        } else if (levelCombo.getSelectedIndex() == 1) {
            level = 9;

        } else if (levelCombo.getSelectedIndex() == 2) {
            level = 6;
        }
        System.out.println(level);
    }//GEN-LAST:event_levelComboItemStateChanged

    public void init() throws IOException {
        droppablePanel.setController(controller);
        Timer threeSec = new Timer();
        TimerTask serverCheck = new TimerTask() {
            @Override
            public void run() {

                boolean serverAv;
                serverAv = SimController.pingServer(controller.retrieveServerAddress());

                if (serverAv) {
                    serverNotification.setForeground(Color.GREEN);
                    serverNotification.setText("Networked!!");
                } else {
                    serverNotification.setForeground(Color.RED);
                    serverNotification.setText("Local");
                }
            }
        };
        threeSec.schedule(serverCheck, 1000);

        AttachCheckBoxes(themeMenu);

        //make GridPanel Components draggable;
        componentDrag = new ComponentDrag(droppablePanel, controller, this);
        // make the JPanel draggable
        DnDpaneldrop dnDpaneldrop = new DnDpaneldrop(this, droppablePanel, controller);
        //make the list draggable
        DnDlistDrag dndListDrag = new DnDlistDrag(availableCompList, droppablePanel);

        // check if the FirstLogged user is completePhone3D valid User and return completePhone3D SimUser Object Once, then change security icon
        presentSimUser = controller.getLoggedInUser(System.getProperty("user.name"));
        if (presentSimUser != null) { // put completePhone3D locked image on image status
            reloadLoginImage(true);
            controller.setSimUser(presentSimUser);
        } else {
            reloadLoginImage(false);
        }

        LoggedLabel.setText("GUEST");// and also setIcon!!
//        SimState[]  recentStates = controller.fetchSessions(true);
//              for(int i=0;i<recentStates.length;i++){           
//              recentMenu.add(new JMenu(recentStates[i].toString()));              
//              }
    }

    public void login() {
        //TODO Obtain user info
        controller.login(userName.getText(), new String(password.getPassword()));

    }

    public void startSimulation() {

        controller.startSimulation(null, "hjgfshjgijkhsdf");
        controller.getState().setSimUser(getPresentSimUser());
        controller.setDropOccured(false);

        //TODO Init JLists availableComponents and usedComponents
        controller.getState().addObserver(this);
        controller.getState().notifyObservers();
    }

    public void open() {

        controller.getState().addObserver(this);
        controller.getState().notifyObservers();
    }

    public void addComponent() {
        try {
            controller.addComponent((SimComponent) availableCompList.getSelectedValue());
        } catch (SimException ex) {
            Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void undo() {
        controller.undo();
    }

    public void redo() {
        controller.redo();
    }

    public JLabel getUserLogged() {
        return LoggedLabel;
    }

    public void setUserLogged(JLabel userLogged) {
        this.LoggedLabel = userLogged;
    }

    public SimUser getPresentSimUser() {
        return presentSimUser;
    }

    public void setPresentSimUser(SimUser presentSimUser) {
        this.presentSimUser = presentSimUser;
    }

    public JMenu getRecentMenu() {
        return recentMenu;
    }

    public void setRecentMenu(JMenu recentMenu) {
        this.recentMenu = recentMenu;
    }

    public ComponentDrag getComponentDrag() {
        return componentDrag;
    }

    public void setComponentDrag(ComponentDrag componentDrag) {
        this.componentDrag = componentDrag;
    }

    public void save() {
        //TODO Persist state to database
        controller.save();

    }

    public void exit() {
        //TODO Clean SimState and open connections and exit
        controller.exit();
    }

    public void print() {
        //TODO send   visual to printer
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print WorkSpace/3D view");

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
                if (pageIndex > 0) {
                    return (NO_SUCH_PAGE);
                } else {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.translate(pageFormat.getImageableX(),
                            pageFormat.getImageableY());

                    jPanel1.paint(g2d);

                    return (PAGE_EXISTS);
                }
            }
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, e);
            }
        }

    }

    public boolean isRerender() {
        return rerender;
    }

    public void setRerender(boolean rerender) {
        this.rerender = rerender;
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Call any of the paint methods to redraw the view
        Object[] updates = (Object[]) arg;

        String directive = (String) updates[0];

        if (directive.equals("placeComponent")) {
            SimComponent simComp = (SimComponent) updates[1];
            defaultListModelAvailable.removeElement(simComp);
            defaultListModelUsed.addElement(simComp);
            droppablePanel.repaint();
        } else if (directive.equals("moveOpenphase")) {
            selectModuleTypeCombo.setSelectedIndex(0);
            moduleCombo.setSelectedIndex(0);
            selectModuleTypeCombo.setEnabled(false);
            moduleCombo.setEnabled(false);
            // getLoggedLabel().setText(sUser.getUsername());
            // setPresentSimUser(sUser);
            // getLoggedLabel().setText(sUser.getUsername());
            descDialogTxt.setText(null);
            sessionDescLabel.setText("None");
            controller.setLoginClear(true);
            defaultListModelAvailable.removeAllElements();
            defaultListModelUsed.removeAllElements();
            droppablePanel.repaint();
            controller.setState(null);
            moduleCombo.removeAllItems();
            // Open open panel
            PopUpFrame temp = popupFrame.addOpenPanel(openPanel, this);
            openPanel.setSize(300, 150);
            temp.setTitle("KSimulator");
            try {
                temp.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
            } catch (IOException ex) {
                Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
            }
            //temp.setSize(openPanel.getSize());
            temp.setVisible(true);
            controller.setInstigClear(false);

        } else if (directive.equals("deleteUndo")) {
            SimComponent sC = (SimComponent) updates[1];
            defaultListModelAvailable.removeElement(sC);
            defaultListModelUsed.addElement(sC);
            droppablePanel.repaint();

        } else if (directive.equals("OpenComponent")) {
            if (controller.getState().isTimed()) {
                savedTime = controller.getState().getTimerDuration();
                timer.setSelected(controller.getState().isTimed());
                timerLabel.setText(sdm.format(new Date(savedTime)));
                levelCombo.setSelectedItem(controller.getState().getLevel());
                scoreText.setText(Double.toString(controller.getState().getScore()));
                timer.setEnabled(false);
            }
            droppablePanel.repaint();

        } else if (directive.equals("Undo")) {
            droppablePanel.repaint();

        } else if (directive.equals("updateStatus")) {

            updateStatus.setText((String) updates[1]);
//            OkOption ok = new OkOption(this, (String) updates[1]);
//            ok.showDialog();

        } else if (directive.equals("clearAction")) {
            SimComponent compCleared = (SimComponent) updates[1];
            defaultListModelUsed.removeAllElements();
            defaultListModelAvailable.addElement(compCleared);
        } else if (directive.equals("clearClicked")) {
            availableCompList.setFocusable(false);
            usedComponentsList.setFocusable(false);
            this.setFocusable(true);
            highlightedComponent = (SimComponent) updates[1];
        } else if (directive.equals("changeUserNoState")) {
            SimUser sUser = (SimUser) updates[1];
            getLoggedLabel().setText(sUser.getUsername());
            controller.setSimUser(sUser);
            setPresentSimUser(sUser);
        } else if (directive.equals("changeUser")) {
            SimUser sUser = (SimUser) updates[1];
            YesNoOption yes_no = new YesNoOption(this);
            yes_no.setLabel1("Do You Want To Clear Available Session?");
            yes_no.setSize(300, 100);
            int keyOp = yes_no.showDialog();
            switch (keyOp) {
                case 1:
                    controller.setSimUser(sUser);
                    selectModuleTypeCombo.setSelectedIndex(0);
                    moduleCombo.setSelectedIndex(0);
                    selectModuleTypeCombo.setEnabled(false);
                    moduleCombo.setEnabled(false);
                    getLoggedLabel().setText(sUser.getUsername());
                    setPresentSimUser(sUser);
                    getLoggedLabel().setText(sUser.getUsername());

                    descDialogTxt.setText(null);
                    sessionDescLabel.setText("None");
                    controller.setLoginClear(true);
                    defaultListModelAvailable.removeAllElements();
                    defaultListModelUsed.removeAllElements();
                    droppablePanel.repaint();
//                controller.updateStatustext("Please Start a New Session To Continue");
                    OkOption ok = new OkOption(this, "KSimulator");
                    ok.setLabel1("Please Start a New Session To Continue");
                    ok.setSize(300, 100);
                    ok.showDialog();
                    controller.setState(null);
                    moduleCombo.removeAllItems();
                    break;
                case 2:
                case 3:
            }

        } else if (directive.equals("LogOut")) {
            String homeUser = (String) updates[1];
//            int response= JOptionPane.showConfirmDialog(null,"Do You Want To Clear Available Session?","Clear Session?",JOptionPane.YES_NO_OPTION);
            YesNoOption yes_no = new YesNoOption(this);
            yes_no.setLabel1("Do You Want To Clear Available Session?");
            yes_no.setSize(300, 100);
            int keyOp = yes_no.showDialog();
            switch (keyOp) {
//                controller.getDummyState().notifyObservers();
                case 1:
                    getLoggedLabel().setText(homeUser);
                    setPresentSimUser(null);
                    descDialogTxt.setText(null);
                    sessionDescLabel.setText("None");
                    controller.setLoginClear(true);
                    defaultListModelAvailable.removeAllElements();
                    defaultListModelUsed.removeAllElements();
                    moduleCombo.removeAllItems();
                    moduleCombo.setEnabled(false);
                    selectModuleTypeCombo.removeAllItems();
                    selectModuleTypeCombo.setEnabled(false);
                    droppablePanel.repaint();
//                controller.updateStatustext("Please Start a New Session To Continue");
                    OkOption ok = new OkOption(this, "KSimulator");
                    ok.setLabel1("Please Start a New Session To Continue");
                    ok.setSize(300, 100);
                    ok.showDialog();
                    controller.setState(null);

                    break;
                case 2:
                case 3:

            }

        }

    }

    // methodTo populate component successor point
    public void populateSuccessorLocation(SimComponent sc) {
    }

    class CustomizeDeletePopup extends JPopupMenu {

        JMenuItem aboutItem, removeItem, helpItem;
        JPanel componentInfoPanel;
        JLabel componentLabel;
        JTextArea descriptionText;

        public CustomizeDeletePopup(final SimComponent comp, final SimController controller) throws IOException {
//      final SimComponent  comp = (SimComponent) usedComponentsList.getSelectedValue();
            componentInfoPanel = new JPanel();
            componentLabel = new JLabel();
            componentLabel.setIcon(comp.getDescriptionIconImage());
            descriptionText = new JTextArea();
            descriptionText.setLineWrap(true);
            descriptionText.setColumns(22);
            descriptionText.setText(comp.getDescription());
            descriptionText.setEditable(false);

            componentInfoPanel.add(componentLabel);
            componentInfoPanel.add(descriptionText);
            aboutItem = new JMenuItem("About " + comp.getComponentName());

            aboutItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {

                    try {
                        PopUpFrame custPopupFrame = new PopUpFrame();
                        custPopupFrame.addPanels(componentInfoPanel);
                        custPopupFrame.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
                        custPopupFrame.setVisible(true);
                    } catch (IOException ex) {
                        Logger.getLogger(CustomizeDeletePopup.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            removeItem = new JMenuItem("Remove");

            removeItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    removeUsed(comp, event);
                    controller.setSaved(false);
                }
            });

            helpItem = new JMenuItem("Help");

            add(aboutItem);
            addSeparator();
            add(removeItem);

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                SplashScreen sc = new SplashScreen();
                sc.setVisible(true);

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog CorrectlyPlacedDialog;
    private javax.swing.JLabel LoggedLabel;
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.JList availableCompList;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel clearWorkSpaceSubMenu;
    private javax.swing.JPanel componentListPanel;
    private javax.swing.JPanel deleteOption;
    private javax.swing.JTextField descDialogTxt;
    private javax.swing.JButton descripDialogBtn;
    private javax.swing.JDialog descriptionDialog;
    private javax.swing.JCheckBoxMenuItem edCheckBox;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel infoShortcut;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JComboBox levelCombo;
    private javax.swing.JMenuItem licenseItem;
    private javax.swing.JLabel logiconLabel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JComboBox moduleCombo;
    private javax.swing.JMenuItem newItem;
    private javax.swing.JLabel newSubMenu;
    private javax.swing.JLabel noDescTypedError;
    private javax.swing.JLabel open;
    private javax.swing.JMenuItem openItem;
    private javax.swing.JMenuItem printItem;
    private javax.swing.JLabel printSubMenu;
    private javax.swing.JMenu recentMenu;
    private javax.swing.JMenuItem redoItem;
    private javax.swing.JLabel redoSubMenu;
    private javax.swing.JLabel removeUsed;
    private javax.swing.JDialog saveDialog;
    private javax.swing.JButton saveDialogcancel;
    private javax.swing.JButton saveDialogdont;
    private javax.swing.JButton saveDialogsave;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JLabel saveShortcut;
    private javax.swing.JLabel scoreLabel;
    private javax.swing.JLabel scoreText;
    private javax.swing.JComboBox selectModuleTypeCombo;
    private javax.swing.JLabel serverNotification;
    private javax.swing.JLabel sessionDescLabel;
    private javax.swing.JLabel sessionDescritionLabel;
    private javax.swing.JLabel sessionName;
    private javax.swing.JLabel sessionStaticText;
    private javax.swing.JPanel simulationPanel;
    private javax.swing.JLabel status;
    private javax.swing.JMenu themeMenu;
    private javax.swing.JCheckBox timer;
    private javax.swing.JLabel timerLabel;
    private javax.swing.JMenuItem tipsItem;
    private javax.swing.JMenuItem undoItem;
    private javax.swing.JLabel undoSubMenu;
    private javax.swing.JLabel updateStatus;
    private javax.swing.JLabel updatechecked;
    private javax.swing.JList usedComponentsList;
    private javax.swing.JMenuItem videoItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JCheckBoxMenuItem workCheckBox;
    private javax.swing.JPanel workSpacePanel;
    private javax.swing.JButton yenocancel;
    private javax.swing.JLabel yesnoLabel;
    private javax.swing.JDialog yesnocancel;
    private javax.swing.JButton yesnono;
    private javax.swing.JButton yesnoyes;
    // End of variables declaration//GEN-END:variables
    /**
     *
     * @author DARE
     */
//}

    class DnDlistDrag implements DragGestureListener, DragSourceListener {

        DragSource source;
        JPanel mainDropPanel;
        TransferableSimComponent transferable;
        JList listTemporary;
        Toolkit toolkit;

        public DnDlistDrag(JList list, JPanel dropPanel) {
            listTemporary = list;
            toolkit = Toolkit.getDefaultToolkit();
            mainDropPanel = dropPanel;
            source = new DragSource();
            source.createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_COPY, this);
        }

        @Override
        public void dragGestureRecognized(DragGestureEvent dge) {
            try {
                transferable = new TransferableSimComponent((SimComponent) listTemporary.getSelectedValue());

                SimComponent draggedComponent = (SimComponent) transferable.getTransferData(TransferableSimComponent.COMPONENT_FLAVOR);
                Cursor customCursor = toolkit.createCustomCursor(draggedComponent.getSolidImage(), new Point(15, 15), "MyCursor");
                mainDropPanel.setCursor(customCursor);
                source.startDrag(dge, customCursor, transferable, this);
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(DnDlistDrag.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DnDlistDrag.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void dragEnter(DragSourceDragEvent dsde) {
        }

        @Override
        public void dragOver(DragSourceDragEvent dsde) {
        }

        @Override
        public void dropActionChanged(DragSourceDragEvent dsde) {
        }

        @Override
        public void dragExit(DragSourceEvent dse) {
        }

        @Override
        public void dragDropEnd(DragSourceDropEvent dsde) {
            mainDropPanel.setCursor(null);
        }
    }

// The  Drop Class
    class DnDpaneldrop extends TransferHandler implements DropTargetListener {

        private DropTarget target;
        private SimController simController;
        private JFrame frameTemp;

        public DnDpaneldrop(JFrame frame, JPanel drop, SimController sc) {
            frameTemp = frame;
            target = new DropTarget(drop, this);
            simController = sc;

        }

        @Override
        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
            return super.canImport(comp, transferFlavors);
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
        }

        public void dropActionchanged(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
//       JOptionPane.showConfirmDialog(null, "You Have Dragged Component Out of Drop Area!!", "Drag Out Of Worspace Alert", JOptionPane.OK_OPTION);
            OkOption ok = new OkOption(null, "KSimulator");
            ok.setLabel1("Cannot place component outside Workspace area");
            ok.setSize(350, 100);
            ok.showDialog();
        }

        //This is what happens when completePhone3D Drop occurs
        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {

                System.out.println("Dropped wrong");
                frameTemp.setCursor(null);
                simController.setEmptyPlacedComponent(false);
//            simController.setSave(SimController.SaveState.NotSaved);
                simController.setSaved(false);
                Transferable tr = dtde.getTransferable();
                SimComponent dropped = (SimComponent) tr.getTransferData(TransferableSimComponent.COMPONENT_FLAVOR);
                if (dtde.isDataFlavorSupported(TransferableSimComponent.COMPONENT_FLAVOR) || dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);

                    SimPoint tempPoint = new SimPoint(dtde.getLocation().x - 130, dtde.getLocation().y - 110);
                    //test codes to show the list of successor comp
                    double maxX = tempPoint.getTopX() + 210;
                    double maxY = tempPoint.getTopY() + 210;
                    if (maxX > 1000) {
                        tempPoint.setTopX(tempPoint.getTopX() - 130);

                    }
                    if (tempPoint.getTopY() < 0) {
                        tempPoint.setTopY(0);
                    }
                    if (tempPoint.getTopX() < 0) {
                        tempPoint.setTopX(0);
                    }

                    simController.calculateFreshDropOverlapPercentage(dropped, tempPoint);
                    simController.placeComponent(dropped, tempPoint);
                    simController.setDropOccured(true);

                    if (simController.getSimUser() == null) { // whe a user isnt logged in
                        simController.createModification("guestlogger.klog", dropped.getComponentName(), "GUEST", "drop", System.currentTimeMillis(), tempPoint.toString());
                        simController.splitUndoRedoLog("guestlogger.klog");
                    } else {
                        simController.createModification("logger.klog", dropped.getComponentName(), simController.getSimUser().getFirstName(), "drop", System.currentTimeMillis(), tempPoint.toString());
                        simController.splitUndoRedoLog("logger.klog");
                    }

                    dtde.dropComplete(true);
                    //ask to see
                    if (timer.isSelected()) {
                        dropTimes++;
                        if (dropTimes == 1) {
                            start = true;
                            startTime = System.currentTimeMillis();
                            droppingTime = startTime;
                            simController.getState().setTimerStart(new Date(startTime));
                            if (thread == null) {
                                runner = new Runnable() {
                                    @Override
                                    public void run() {
                                        timer();
                                    }
                                };
                                thread = new Thread(runner);
                                thread.start();

                            }
                            score = dropped.getScorePoint();
                            scoreLabel.setText(Double.toString(score));
                            System.out.println("score is " + score);
                        }
                    }
//                else if(dropTimes ==7){
//                start = false;
//                }
                    return;
                }
                dtde.rejectDrop();
            } catch (UnsupportedFlavorException | IOException ex) {
                //Logger.getLogger(DnDpaneldrop.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
// class or cellrendering

    class IconListRenderer implements ListCellRenderer {

        DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        IconListRenderer() {
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            Border border = BorderFactory.createTitledBorder("");

            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
            SimComponent s = (SimComponent) value;

            renderer.setBorder(BorderFactory.createTitledBorder(""));

            try {
                renderer.setIcon(s.getIconImage());
            } catch (IOException ex) {
                Logger.getLogger(IconListRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }

            return renderer;
        }
    }

// START Cutomized JPopUpMenu class for the Available/Used Component Jlist 
    class CustomisePopUp extends JPopupMenu {

        private JMenuItem aboutMenuItem;
        private JMenuItem helpMenuItem;
        private JLabel componentIcon;
        private JLabel componentDescription;
        private SimComponent customSimComponent;
        private JPanel componentInfoPanel;

        public CustomisePopUp(boolean popUp, SimComponent simComponent) throws IOException {
            //if its true generate completePhone3D menu pop up if its force general description

            componentIcon = new JLabel("");
            aboutMenuItem = new JMenuItem();
            helpMenuItem = new JMenuItem("Help");
            componentIcon = new JLabel();
            componentDescription = new JLabel();
            componentInfoPanel = new JPanel();

            customSimComponent = simComponent;
            componentIcon.setIcon(customSimComponent.getDescriptionIconImage());
            componentDescription.setText(customSimComponent.getDescription());
            aboutMenuItem.setText("About " + customSimComponent.getComponentName());

            componentInfoPanel.add(componentIcon);
            componentInfoPanel.add(componentDescription);
            if (popUp) {
                // Click PopUp Option
                aboutMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            PopUpFrame custPopupFrame = new PopUpFrame();
                            custPopupFrame.addPanels(componentInfoPanel);
                            custPopupFrame.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
                            custPopupFrame.setVisible(true);
                        } catch (IOException ex) {
                            Logger.getLogger(CustomisePopUp.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

            } else {
                // hot keys Option
                PopUpFrame custPopupFrame = new PopUpFrame();
                custPopupFrame.addPanels(componentInfoPanel);
                custPopupFrame.setIconImage(ImageIO.read(this.getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/kicon.png")));
                custPopupFrame.setVisible(true);

            }

            this.add(aboutMenuItem);
            this.addSeparator();
            this.add(helpMenuItem);

        }
    }

//START GridLine CLass
    class GridPanel extends JPanel {

        SimController gController;

        GridPanel() {
            setOpaque(false);

        }

        public void setController(SimController gridController) {
            gController = gridController;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.LIGHT_GRAY);

//draw vertical lines
            for (int i = 5; i <= this.getSize().width; i += 8) {
                g.drawLine(i, 0, i, this.getSize().height);
            }

// draw X axIs
            for (int i = 0; i <= this.getSize().height; i += 8) {
                g.drawLine(0, i, this.getSize().width, i);
            }
        // take clearAction

            //paint images
            if (gController.IsOpenDirective()) {
                Map<SimComponent, SimPoint> placedComponents = gController.getState().getPlacedComponents();

                Iterator<SimComponent> iterator = placedComponents.keySet().iterator();

                while (iterator.hasNext()) {
                    SimComponent tempIterated = iterator.next();
                    try {
                        g.drawImage(tempIterated.getWireframeImage(),
                                (int) placedComponents.get(tempIterated).getTopX(), (int) placedComponents.get(tempIterated).getTopY(), null);
                    } catch (IOException ex) {
                        Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            } else {

                try {
                    if (gController.IsEmptyPlacedComponent()) {
                        // this is done to check the placed Component null pinter exception  
                        // do nothing
                    } else {
                        if (gController.isClearAction()) {
                            Map<SimComponent, SimPoint> tempMap = new LinkedHashMap();
                            g.drawImage(null, 0, 0, this);
                            gController.returnClearedComponent();
                            gController.getState().setPlacedComponents(tempMap);
                            gController.setClearAction(false);
                        } else if (gController.isLoginClear()) {
                            g.drawImage(null, 0, 0, this);

                        } else {
                            Map<SimComponent, SimPoint> placedComponents = gController.getState().getPlacedComponents();

                            Iterator<SimComponent> iterator = placedComponents.keySet().iterator();

                            //paint only last component
                            while (iterator.hasNext()) {
                                SimComponent painted = iterator.next();
                                g.drawImage(painted.getWireframeImage(),
                                        (int) placedComponents.get(painted).getTopX(), (int) placedComponents.get(painted).getTopY(), null);
                            }

                        }

                    }
                } catch (IOException ex) {
                    Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
//End GridLine CLass

//Component Drag Class
    class ComponentDrag extends MouseAdapter implements MouseListener {

        Component3dApplication n;
//    private static final Logger LOGGER = Logger.getLogger(ComponentDrag.class.getName());
        private final Logger LOGGER = Logger.getLogger(ComponentDrag.class.getName());
        GridPanel draggablePanel;
        SimController dragController;
        JFrame dragFrame;
        double sourceX;
        double sourceY;
        SimComponent draggedComponent;
        Point cursorOffset;
        List<SimComponent> multiDraggedComponents;
        boolean componentMovement = false;
        boolean isAMultiDrag = false;
        boolean isEmptyDrag = false;
        Toolkit tk;
        SimComponent succeededComponent;
        SimPoint baseSimPoint;
        SimComponent corrrectlyPlacedComponents;
        Map<SimComponent, SimPoint> placedComp;
        List<SimComponent> listofCorrectlyPlaced = null;

        public ComponentDrag(GridPanel panel, SimController lsc, JFrame frame) {
            multiDraggedComponents = new ArrayList<SimComponent>();
            draggablePanel = panel;
            dragController = lsc;
            dragFrame = frame;
            tk = Toolkit.getDefaultToolkit();
            draggablePanel.addMouseListener(this);
            draggablePanel.addMouseMotionListener(this);
            listofCorrectlyPlaced = new ArrayList<SimComponent>();
        }

        public double getSourceX() {
            return sourceX;
        }

        public void setSourceX(double sourceX) {
            this.sourceX = sourceX;
        }

        public double getSourceY() {
            return sourceY;
        }

        public void setSourceY(double sourceY) {
            this.sourceY = sourceY;
        }

        public SimComponent getSucceededComponent() {
            return succeededComponent;
        }

        public void setSucceededComponent(SimComponent succeededComponent) {
            this.succeededComponent = succeededComponent;
        }

        public SimController getDragController() {
            return dragController;
        }

        public void setDragController(SimController dragController) {
            this.dragController = dragController;
        }

        public SimPoint getBaseSimPoint() {
            return baseSimPoint;
        }

        public void setBaseSimPoint(SimPoint baseSimPoint) {
            this.baseSimPoint = baseSimPoint;
        }

        public SimComponent getCorrrectlyPlacedComponents() {
            return corrrectlyPlacedComponents;
        }

        public void setCorrrectlyPlacedComponents(SimComponent corrrectlyPlacedComponents) {
            this.corrrectlyPlacedComponents = corrrectlyPlacedComponents;
        }

        public Map<SimComponent, SimPoint> getPlacedComp() {
            return placedComp;
        }

        public void setPlacedComp(Map<SimComponent, SimPoint> placedComp) {
            this.placedComp = placedComp;
        }

        public List<SimComponent> getListofCorrectlyPlaced() {
            return listofCorrectlyPlaced;
        }

        public void setListofCorrectlyPlaced(List<SimComponent> listofCorrectlyPlaced) {
            this.listofCorrectlyPlaced = listofCorrectlyPlaced;
        }

        @Override
        public void mousePressed(MouseEvent me) {
            try {
                Map<SimComponent, SimPoint> placedComponents = dragController.getState().getPlacedComponents();
                draggedComponent = dragController.getDraggedComponent(dragController.getState().getPlacedComponents(), new SimPoint(me.getX(), me.getY()));
                if (me.isShiftDown()) {

                    if (dragController.isMultiDrag(new SimPoint(me.getX(), me.getY()))) {
                        isEmptyDrag = false;
                        multiDraggedComponents = dragController.getDraggedComponents(placedComponents,
                                new SimPoint(me.getX(), me.getY()));
                    } else {
                        isEmptyDrag = true;
//                    dragController.updateStatustext("Illegal Drag Operation! u cant do that");
                        OkOption ok = new OkOption(null, "KSimulator");
                        ok.setLabel1("Illegal Drag Operation! You can not do that");
                        ok.setSize(350, 100);
                        ok.showDialog();
                    }

                }
            } catch (NullPointerException nul) {
                Logger.getLogger(SimView.class.getName()).log(Level.SEVERE, null, "First mouse click");
            }
        }

        @Override
        public void mouseMoved(MouseEvent me) {
//        try {
//            Thread.sleep(200);
//            if (!dragController.IsEmptyPlacedComponent() && dragController.getState().getPlacedComponents() != null && me != null) {
//                SimComponent c = dragController.getDraggedComponent(dragController.getState().getPlacedComponents(), new SimPoint(me.getX(), me.getY()));
//                if (c != null) {
//        ss            dragController.updateStatustext(c.getComponentName());
//                }
//            } else {
//                dragController.updateStatustext("No Component Selected");
//
//            }
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ComponentDrag.class.getName()).log(Level.SEVERE, null, ex);
//        }
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            Map<SimComponent, SimPoint> placedComponents = dragController.getState().getPlacedComponents();
            dragController.setSaved(false);
            if (!me.isShiftDown() && (me.getX() > 100 && me.getY() > 100 && me.getX() < 800 && me.getY() < 470)) {

                placedComponents.get(draggedComponent).setTopX(me.getX() - 100);
                placedComponents.get(draggedComponent).setTopY(me.getY() - 100);
                try {
                    if (dragController.getSimUser() == null) { // whe a user isnt logged in
                        dragController.createModification("guestlogger.klog", draggedComponent.getComponentName(), "GUEST",
                                "dragged", System.currentTimeMillis(), new SimPoint(placedComponents.get(draggedComponent).getTopX(), placedComponents.get(draggedComponent).getTopY()).toString());
                        dragController.splitUndoRedoLog("guestlogger.klog");
                    } else {
                        dragController.createModification("logger.klog", draggedComponent.getComponentName(), dragController.getSimUser().getFirstName(),
                                "dragged", System.currentTimeMillis(), new SimPoint(placedComponents.get(draggedComponent).getTopX(), placedComponents.get(draggedComponent).getTopY()).toString());
                        dragController.splitUndoRedoLog("logger.klog");
                    }

                } catch (NullPointerException ex) {
                    System.out.println("NullpointerException on 2946");
                }
            } else {
                if (!isEmptyDrag) {
                    for (int i = 0; i < multiDraggedComponents.size(); i++) {
                        placedComponents.get(multiDraggedComponents.get(i)).setTopX(me.getX() - 100);
                        placedComponents.get(multiDraggedComponents.get(i)).setTopY(me.getY() - 100);
                    }
                }
                //loop to change multiposition!
            }
            draggablePanel.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            System.out.println("wrong drop");
            //Toolkit toolkit = Toolkit.getDefaultToolkit();
            draggablePanel.setCursor(null);
            // on mouse release it doesnt just place it checks 
            // 1. if its completePhone3D successor
            // 2. if its 40% overlapped

            if (dragController.getState() != null) {
                succeededComponent = dragController.getComponentToBeSucceeded(dragController.getState().
                        getPlacedComponents(), new SimPoint(evt.getX(), evt.getY()), draggedComponent);

                /////////////////////// if its completePhone3D single component
                //
                //
                //  THIS SECTION HOLDS ONLY THE SINGLE DRAG JOBS
                //
                //
                //
                /////////////////////////////////////////////////////////
                if (!isAMultiDrag) {
                    if (succeededComponent != null) {
                        if (checkQualification(succeededComponent, draggedComponent, evt) && !checkSame(succeededComponent, draggedComponent, evt)) {
//                        int numberComplete = 0;
                            System.out.println("correctly");
                            System.out.println("How long was " + howLong);
                            baseSimPoint = dragController.getBaseSimPoint(succeededComponent);

                            dragController.getState().getPlacedComponents().get(draggedComponent).setTopX(baseSimPoint.getTopX());
                            dragController.getState().getPlacedComponents().get(draggedComponent).setTopY(baseSimPoint.getTopY());

                            dragFrame.repaint();

                            dragController.updateStatustext("Component placed correctly");

                            if (timer.isSelected()) {
                                howLong = (System.currentTimeMillis() - droppingTime) / 1000;
                                droppingTime = System.currentTimeMillis();
                                howLong = Math.rint(howLong);

                                if (!placedList.contains(draggedComponent)) {
                                    if (howLong <= level) {
                                        score = score + draggedComponent.getScorePoint();
                                    } else if (howLong > level) {
                                        score = score + ((draggedComponent.getScorePoint()) - ((howLong - level) * 0.01) * 1.5);

                                        placedList.add(draggedComponent);
                                        System.out.println("you added " + draggedComponent.getComponentName());
//                                System.out.println("Your score is "+scores);
                                        System.out.println("it took  " + howLong);

                                        howLong = 0;
                                    }
                                    DecimalFormat dform = new DecimalFormat("00.00");
                                    scores = dform.format(score);
                                    scoreLabel.setText(scores);
                                }
                            }
//                        int response = JOptionPane.showConfirmDialog(dragFrame, "Component3dApplication 3D image?",
//                                "Componenent Placed Correctly", JOptionPane.OK_CANCEL_OPTION);
                            int listof = 0;
                            int numberComplete = 0;
                            YesNoOption yesno = new YesNoOption(dragFrame);
                            yesno.setLabel1("Component placed correctly. View 3D image?");
                            yesno.setSize(350, 100);
                            int response = yesno.showDialog();

                            if (response == 1) {

                                //get the top X axis ofcorrectly placed base component to compare with other
                                double correctlyPlacedPositionTopX = baseSimPoint.getTopX();

                                //map all placed components and there simpoint
                                placedComp = dragController.getState().getPlacedComponents();
                                //Iterate through placed component map to retrieve components with same Simpoints
                                Iterator<SimComponent> iterator = placedComp.keySet().iterator();
                                listofCorrectlyPlaced = new ArrayList<>();

                                while (iterator.hasNext()) {

                                    for (int i = 0; i < placedComp.size(); i++) {
                                        SimComponent iteratedComp = iterator.next();
//                                    compare all placed components and make sure that only components with the correct topX are placed 
                                        if (placedComp.get(iteratedComp).getTopX() == correctlyPlacedPositionTopX) {
                                            numberComplete += 1;
                                            listof += 1;
                                            listofCorrectlyPlaced.add(iteratedComp);
                                            corrrectlyPlacedComponents = iteratedComp;
                                        }
                                    } // End For Loop
                                    //This is method was not  used
                                    if (numberComplete == placedComp.size() && dragController.getState().getAvailableComponents().isEmpty()) {
                                        start = false;
                                        dragController.updateStatustext("Simulation Complete!!");
                                        OkOption okk = new OkOption(null, "KSimulator");
                                        okk.setLabel1("Assembly Complete");
                                        okk.setSize(150, 100);
                                        okk.showDialog();

                                    }

                                }

                                ////////////// while ends
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Component3dWindow customJFrame = new Component3dWindow(listofCorrectlyPlaced);
                                        } catch (HeadlessException ex) {
                                            Logger.getLogger(ComponentDrag.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(ComponentDrag.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                });
                                //          threedFrame = new ViewFor3d(new ArrayList<SimComponent>(listofCorrectlyPlaced),new HashMap<SimComponent, BranchGroup>(dragController.getScenesAll()));
                                //
                                //                            Thread jmeThread = new Thread(new Runnable() {
                                //                                @Override
                                //                                public void run() {
                                //                                    threedFrame.setLocationRelativeTo(null);
                                //                                   threedFrame.setVisible(true);
                                //
                                //                                }
                                //                            });
                                //                            jmeThread.start();
                                //                            dragFrame.repaint();

                            } else if (dragController.getState().getAvailableComponents().isEmpty()) {
                                if (dragController.checkSameSimPoints()) {
                                    start = false;
                                    stopTime = System.currentTimeMillis();
                                    System.out.println("this is near complete");
                                    dragController.updateStatustext("Simulation Complete!!");
//                            JOptionPane.showMessageDialog(null, "Simulation Complete");
                                    OkOption okk = new OkOption(null, "KSimulator");
                                    okk.setLabel1("Assembly Complete");
                                    okk.setSize(150, 100);
                                    okk.showDialog();
                                    System.out.println("Your final score is " + score);
                                    scoreLabel.setText(scores);
                                    canSave = true;
                                    if (timer.isSelected()) {
                                        dragController.getState().setTimerDuration(stopTime - startTime);
                                        dragController.getState().setLevel(levelCombo.getSelectedItem().toString());
                                        dragController.getState().setTimed(true);
                                        dragController.getState().setScore(Double.parseDouble(scores));
                                        System.out.println(dragController.getState().getScore());
                                    }

                                }

                            }
                        } else if (checkSame(succeededComponent, draggedComponent, evt)) {
                            dragController.updateStatustext("You haven't Moved "
                                    + draggedComponent.getComponentName() + "out of its Area");

                        } else if (!checkQualification(succeededComponent, draggedComponent, evt)) {
                            Toolkit.getDefaultToolkit().beep();
                            dragController.updateStatustext("Can not drop " + "'" + draggedComponent.getComponentName() + "'" + "  on " + "'" + succeededComponent.getComponentName() + "'");
                            OkOption ok1 = new OkOption(null, "KSimulator");
                            ok1.setLabel1("Can't drop " + "'" + draggedComponent.getComponentName() + "'" + "  on " + "'" + succeededComponent.getComponentName() + "'");
                            ok1.setSize(400, 100);
                            ok1.showDialog();
//                            score = score - (0.4 *draggedComponent.getScorePoint());
                            System.out.println("score is now " + score);
                            if (!placedList.contains(draggedComponent)) {
                                placedList.add(draggedComponent);
                                System.out.println("you placed wrong your score is still " + score);
                            }
                        }

                    } else {
                        // if no successor rules are applied repaint as usual
                        if (cursorOffset != null && draggedComponent != null && dragController != null && dragController.getState() != null && dragController.getState().getPlacedComponents() != null) {
                            SimPoint point = dragController.getState().getPlacedComponents().get(draggedComponent);
                            if (point != null) {
                                point.setTopX(evt.getX() - cursorOffset.x);
                                point.setTopY(evt.getY() - cursorOffset.y);

                                dragFrame.repaint();
                            }
                        }

                    }

                }// when isAmultiDrag equals false
                /////////////////////// if its completePhone3D multiple component
                //
                //
                //  THIS SECTION HOLDS ONLY THE MULTIPLE DRAG JOBS
                //
                //
                //
                /////////////////////////////////////////////////////////
                //When isAmultiDrag equals TRUE
                else {
                    //  get the base Component of the vector of SimComponents if it is completePhone3D succesor to succeding
                    SimComponent succedeesBase = multiDraggedComponents.get(0);
                    if (succeededComponent != null) {
                        if (checkQualification(succeededComponent, succedeesBase, evt)) {
                            baseSimPoint = dragController.getBaseSimPoint(succeededComponent);
                            for (int i = 0; i < multiDraggedComponents.size(); i++) {

                                dragController.getState().getPlacedComponents().get(multiDraggedComponents.get(i)).setTopX(baseSimPoint.getTopX());
                                dragController.getState().getPlacedComponents().get(multiDraggedComponents.get(i)).setTopY(baseSimPoint.getTopY());

                            }// end For statement 
                        } // end if

                    }// END BIGGER IF
                    else {
                        for (int i = 0; i < multiDraggedComponents.size(); i++) {

                            dragController.getState().getPlacedComponents().get(multiDraggedComponents.get(i)).setTopX(evt.getX() - cursorOffset.x);
                            dragController.getState().getPlacedComponents().get(multiDraggedComponents.get(i)).setTopY(evt.getY() - cursorOffset.y);

                        } //ENd FOR 

                    }// END ELSE

                    dragFrame.repaint();
                    isAMultiDrag = false;

                }

            }
        }

        private String[] getListComponentId(List<SimComponent> list) {
            List<String> names = new ArrayList<String>();
            String[] idArray = null;
            if (list.size() > 0) {
                for (SimComponent components : list) {
                    Long id = components.getId();
                    names.add("/obj_" + id.toString() + ".obj");
                }
                idArray = new String[names.size()];
                idArray = names.toArray(idArray);

            }

            return idArray;
        }

        public boolean checkQualification(SimComponent succeededComponent, SimComponent succedding, MouseEvent evt) {

            boolean result = dragController.canSucceed(succeededComponent, succedding);

            double percentageOverlap = dragController.calculateDraggedOverlapPercentage(succeededComponent, evt.getX());

            if (result) {
                // get the baseComponent X and Y  and redraw
                return true;

            } else {
                return false;
            }

        }

        public boolean checkSame(SimComponent succeededComponent, SimComponent succedding, MouseEvent evt) {

            if (succedding.equals(succeededComponent)) {

                return true;

            } else {

                return false;
            }

        }

        public Map<SimComponent, BranchGroup> cloneMapCopy(Map<SimComponent, BranchGroup> mp) {
            Map<SimComponent, BranchGroup> cloned = new HashMap<SimComponent, BranchGroup>();
            cloned.putAll(mp);
            return cloned;
        }
    }
}
// Theme Item ChangedLiastepcjvps

class ThemeChangeListener implements MouseListener {

    private Map<String, String> mapTheme;
    private SimController controller;
    private ImageIcon selectedThemeIcon;
    List<JLabel> listed;

    public ThemeChangeListener(Map<String, String> map, SimController sc, List<JLabel> listed) {
        mapTheme = map;
        controller = sc;
        this.listed = listed;
        selectedThemeIcon = new ImageIcon(getClass().getResource("/com/karmelos/ksimulator/2ndbaricon/helpsmall.png"));
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        JLabel src = (JLabel) me.getSource();
        String selected = src.getText();
        controller.setCurrentTheme(mapTheme.get(selected));
        MarkSelectedTheme(src);

    }

    @Override
    public void mousePressed(MouseEvent me) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //To change body of generated methods, choose Tools | Templates.
    }

    public void MarkSelectedTheme(JLabel selectJLabel) {
        String txt = selectJLabel.getText();
        for (JLabel nxt : listed) {
            if (txt.equals(nxt.getText())) {
                selectJLabel.setIcon(selectedThemeIcon);
            } else {
                nxt.setIcon(null);
            }

        }
        // alert for closing app
        OkOption alert = new OkOption(null, "Ksimulator");
        alert.setMessage("You have to restart Application to effect theme Change");
        alert.setSize(400, 100);
        alert.showDialog();
    }
}
