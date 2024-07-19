package me.Ult1;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.multi.MultiScrollBarUI;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class GUI {

    public int width = 1228, height = 700;
    public JFrame frame;
    public JPanel panel;
    final Color bgColor = new Color(0.1f, 0.1f, 0.1f);

    public GUI(){

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        frame = new JFrame("Ult1's sfx picker");
        ImageIcon img = new ImageIcon(new File("").getAbsolutePath().concat("/src/icon.png"));
        frame.setIconImage(img.getImage());
        System.out.println(new File("").getAbsolutePath());


        panel = new JPanel();
        panel.setBackground(bgColor);
        WrapLayout wl = new WrapLayout();
        wl.setHgap(1);
        wl.setVgap(1);
        panel.setLayout(wl);

        PlaceholderTextField textBox = new PlaceholderTextField();
        textBox.setPlaceholder("Type the name of a file here...");
        textBox.setPreferredSize(new Dimension(width, 40));
        textBox.setBackground(bgColor);
        textBox.setForeground(Color.decode("#55aaff"));
        textBox.setFont(new Font("aria", Font.BOLD, 30));
        textBox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#55aaff")));

        textBox.getDocument().addDocumentListener(new DocumentListener() {

            @Override public void changedUpdate(DocumentEvent arg0) {}
            @Override public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textBox.getText();

                for(String filename : Main.getButtons().keySet()){
                    if(!filename.toLowerCase(Locale.ROOT).contains((text.toLowerCase(Locale.ROOT))))
                        Main.getButtons().get(filename).setVisible(false);
                    else
                        Main.getButtons().get(filename).setVisible(true);
                }

            }
        });


        panel.add(textBox);

        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                textBox.setPreferredSize(new Dimension(componentEvent.getComponent().getSize().width, 40));
            }
        });
        frame.addWindowListener( new WindowAdapter() {
            public void windowOpened( WindowEvent e ){
                textBox.requestFocus();
            }
        });

    }

    public JButton addButton(String text, File file){
        JButton button = new JButton();
        if(text.length() > 30){
            text = text.replaceAll("( {2})( )", "$1<br/>");
        }

        button.setText("<html>" + text.replace(".wav", "") + "</html>");

        button.setBackground(bgColor);
//        button.setForeground(Color.decode("#ffaa55"));
        button.setForeground(Color.decode("#55aaff"));
        button.setFocusPainted(false);
        button.setFont(new Font("Aria", 1, 16));
        button.setPreferredSize(new Dimension(192, 100));


        button.addActionListener(e -> {
            Clip clip = null;
            try {
                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(file));

                new Thread(() -> {
                    try {
                        String cmd = "";
                        if(new File(file.getAbsolutePath().replace("videos_edited_audio", "videos_edited_audio_mp3").replace(".wav", ".mp3")).exists())
                            cmd = "C:\\Users\\Augustas\\Desktop\\Software\\soundboard\\Soundboard.exe -f \"" +
                                file.getAbsolutePath().replace("videos_edited_audio", "videos_edited_audio_mp3").replace(".wav", ".mp3") +
                                "\" -d 2 -v 0.1";


                        System.out.println(cmd);
                        Runtime.getRuntime().exec(cmd);

                    } catch (Exception ignored) {

                    }
                }).start();
            } catch (Exception ignored) { // killing 2 exceptions with one try/catch!
            } // https://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java     i think it's more or less here, the Clip stuff
            clip.start();

            List<File> listOfFiles = new ArrayList();
            listOfFiles.add(file);
            Main.FileTransferable ft = new Main.FileTransferable(listOfFiles);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ft, (clipboard, contents) -> System.out.println("Lost ownership"));
        });

        panel.add(button);
        return button;
    }


    public void finishGUI(){
        frame.add(panel);

        JScrollPane scrPane = new JScrollPane(panel); // scroll pane & bar thing
        JScrollBar bar = new JScrollBar(Scrollbar.VERTICAL);
        bar.setBackground(bgColor);

        scrPane.setVerticalScrollBar(bar);
        scrPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPane.getVerticalScrollBar().setUnitIncrement(32);

        frame.getContentPane().add(scrPane);

        frame.setVisible(true);
    }


}

@SuppressWarnings("serial")
class PlaceholderTextField extends JTextField {

    private String placeholder;

    public PlaceholderTextField() {
    }

    public PlaceholderTextField(
            final Document pDoc,
            final String pText,
            final int pColumns)
    {
        super(pDoc, pText, pColumns);
    }

    public PlaceholderTextField(final int pColumns) {
        super(pColumns);
    }

    public PlaceholderTextField(final String pText) {
        super(pText);
    }

    public PlaceholderTextField(final String pText, final int pColumns) {
        super(pText, pColumns);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    protected void paintComponent(final Graphics pG) {
        super.paintComponent(pG);

        if (placeholder == null || placeholder.length() == 0 || getText().length() > 0) {
            return;
        }

        final Graphics2D g = (Graphics2D) pG;
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        setDisabledTextColor(Color.decode("#3355aa"));
        g.setColor(getDisabledTextColor());
        g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
                .getMaxAscent() + getInsets().top);
    }

    public void setPlaceholder(final String s) {
        placeholder = s;
    }

}