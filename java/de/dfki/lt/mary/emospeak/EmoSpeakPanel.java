/**
 * Copyright 2000-2006 DFKI GmbH.
 * All Rights Reserved.  Use is subject to license terms.
 * 
 * Permission is hereby granted, free of charge, to use and distribute
 * this software and its documentation without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of this work, and to
 * permit persons to whom this work is furnished to do so, subject to
 * the following conditions:
 * 
 * 1. The code must retain the above copyright notice, this list of
 *    conditions and the following disclaimer.
 * 2. Any modifications must be clearly marked as such.
 * 3. Original authors' names are not deleted.
 * 4. The authors' names are not used to endorse or promote products
 *    derived from this software without specific prior written
 *    permission.
 *
 * DFKI GMBH AND THE CONTRIBUTORS TO THIS WORK DISCLAIM ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS, IN NO EVENT SHALL DFKI GMBH NOR THE
 * CONTRIBUTORS BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL
 * DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR
 * PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS
 * ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 */
package de.dfki.lt.mary.emospeak;

import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import de.dfki.lt.mary.client.MaryClient;

/**
 *
 * @author  schroed
 */
public class EmoSpeakPanel extends javax.swing.JPanel
implements AudioFileReceiver, ProsodyXMLDisplayer
{
    private int r=1;
    private EmoTransformer emoTransformer;
    
    
        /* -------------------- Data and Processing stuff -------------------- */
    private AsynchronousThreadedMaryClient asynchronousSynthesiser;
    private MaryClient synchronousSynthesiser;
    private AudioInputStream currentAudio = null;
    private AudioInputStream nextAudio = null;
    private Clip clip = null;
    private boolean userRequestedStop = false;
    private boolean synthesiseAsynchronously;
    private String maryServerHost;
    private int maryServerPort;
    private Map voicesByLocale; // map locale to Vectors of MaryClient.Voice objects
    boolean haveGerman = false;
    private Map sampleTextsByLocale; // map locale to Vectors of Strings
    private Map localeByDisplayLanguage; // map display language to locale
    
    
    /** Creates new form EmoSpeakPanel */
    public EmoSpeakPanel(boolean synthesiseAsynchronously,
        String maryServerHost, int maryServerPort) 
    throws IOException, UnknownHostException {
        this.synthesiseAsynchronously = synthesiseAsynchronously;
        this.maryServerHost = maryServerHost;
        this.maryServerPort = maryServerPort;
        synchronized (this) {
			initComponents();
			customInitComponents();
        }
    }
    
    /** Call this in order to use the emospeakpanel menu */
    public void initialiseMenu() {
        JRootPane rp = getRootPane();
        if (rp != null) rp.setJMenuBar(jMenuBar1);        
    }

    public TwoDimensionalModel feeltraceModel() {
        return ftPanel.feeltraceModel();
    }
    
    public BoundedRangeModel powerModel() {
        return ftPanel.powerModel();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        audioPanel = new javax.swing.JPanel();
        lVoice = new javax.swing.JLabel();
        cbVoice = new javax.swing.JComboBox();
        bPlay = new javax.swing.JButton();
        dimensionValuePanel = new javax.swing.JPanel();
        lActivationValue = new javax.swing.JLabel();
        tfActivationValue = new javax.swing.JTextField();
        lEvaluationValue = new javax.swing.JLabel();
        tfEvaluationValue = new javax.swing.JTextField();
        lPowerValue = new javax.swing.JLabel();
        tfPowerValue = new javax.swing.JTextField();
        inputTextPanel = new javax.swing.JPanel();
        lInputText = new javax.swing.JLabel();
        cbInputText = new javax.swing.JComboBox();
        ftPanel = new JFeeltracePanel();
        lProsodyXML = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tpProsodyXML = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        optionsMenu = new javax.swing.JMenu();
        showPowerMenuItem = new javax.swing.JCheckBoxMenuItem();
        showMaryXMLMenuItem = new javax.swing.JCheckBoxMenuItem();

        setLayout(new java.awt.GridBagLayout());

        // Manually added, Feb. 2006:
        cbLanguage = new JComboBox();
        cbLanguage.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbLanguageItemStateChanged(evt);
            }
        });
        audioPanel.add(cbLanguage);
        lVoice.setText("Voice:");
        audioPanel.add(lVoice);

        cbVoice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbVoiceItemStateChanged(evt);
            }
        });

        audioPanel.add(cbVoice);

        bPlay.setText("Play");
        bPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPlayActionPerformed(evt);
            }
        });

        audioPanel.add(bPlay);

       gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(audioPanel, gridBagConstraints);

        dimensionValuePanel.setLayout(new java.awt.GridLayout(3, 2));

        lActivationValue.setText("Activation");
        dimensionValuePanel.add(lActivationValue);

        tfActivationValue.setText("0");
        tfActivationValue.setPreferredSize(new java.awt.Dimension(40, 21));
        tfActivationValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfActivationValueActionPerformed(evt);
            }
        });

        tfActivationValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfActivationValueFocusLost(evt);
            }
        });

        dimensionValuePanel.add(tfActivationValue);

        lEvaluationValue.setText("Evaluation");
        dimensionValuePanel.add(lEvaluationValue);

        tfEvaluationValue.setText("0");
        tfEvaluationValue.setPreferredSize(new java.awt.Dimension(40, 21));
        tfEvaluationValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfEvaluationValueActionPerformed(evt);
            }
        });

        tfEvaluationValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfEvaluationValueFocusLost(evt);
            }
        });

        dimensionValuePanel.add(tfEvaluationValue);

        lPowerValue.setText("Power");
        dimensionValuePanel.add(lPowerValue);

        tfPowerValue.setText("0");
        tfPowerValue.setPreferredSize(new java.awt.Dimension(40, 21));
        tfPowerValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPowerValueActionPerformed(evt);
            }
        });

        tfPowerValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfPowerValueFocusLost(evt);
            }
        });

        dimensionValuePanel.add(tfPowerValue);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        add(dimensionValuePanel, gridBagConstraints);

        lInputText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lInputText.setText("Text:");
        inputTextPanel.add(lInputText);

        cbInputText.setEditable(true);
        cbInputText.setFont(new java.awt.Font("Dialog", 0, 10));
        cbInputText.setToolTipText("Type or select the text to be spoken.");
        cbInputText.setPreferredSize(new java.awt.Dimension(350, 26));
        cbInputText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbInputTextActionPerformed(evt);
            }
        });

        cbInputText.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbInputTextItemStateChanged(evt);
            }
        });

        inputTextPanel.add(cbInputText);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(inputTextPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        add(ftPanel, gridBagConstraints);

        lProsodyXML.setText("Prosody XML:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        add(lProsodyXML, gridBagConstraints);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 100));
        tpProsodyXML.setBorder(new javax.swing.border.EtchedBorder());
        tpProsodyXML.setEditable(false);
        tpProsodyXML.setPreferredSize(new java.awt.Dimension(400, 100));
        jScrollPane1.setViewportView(tpProsodyXML);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        add(jScrollPane1, gridBagConstraints);

        optionsMenu.setText("Options");
        optionsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsMenuActionPerformed(evt);
            }
        });

        showPowerMenuItem.setSelected(true);
        showPowerMenuItem.setText("Show Power");
        showPowerMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showPowerMenuItemItemStateChanged(evt);
            }
        });

        optionsMenu.add(showPowerMenuItem);
        showMaryXMLMenuItem.setSelected(true);
        showMaryXMLMenuItem.setText("Show Prosody XML");
        showMaryXMLMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showMaryXMLMenuItemItemStateChanged(evt);
            }
        });

        optionsMenu.add(showMaryXMLMenuItem);
        jMenuBar1.add(optionsMenu);

    }//GEN-END:initComponents

    private void tfActivationValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfActivationValueActionPerformed
        updateFeeltraceModelFromTextFields();
    }//GEN-LAST:event_tfActivationValueActionPerformed

    private void tfEvaluationValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfEvaluationValueFocusLost
        updateFeeltraceModelFromTextFields();
    }//GEN-LAST:event_tfEvaluationValueFocusLost

    private void bPlayActionPerformed(java.awt.event.ActionEvent evt) {
        if (bPlay.getText().equals("Play")) {
            try {
                preparePlayAudio();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage(), "Cannot play audio", JOptionPane.ERROR_MESSAGE);
                return;
            }
            bPlay.setText("Stop");
            userRequestedStop = false;
            playAudio();
        } else {
            bPlay.setText("Play");
            userRequestedStop = true;
            if (clip != null) {
                clip.stop();
            }
        }
    }

    private void optionsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsMenuActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_optionsMenuActionPerformed

    private void cbInputTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbInputTextItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) { // new item selected
            requestUpdateProsodyXML();
        }
    }//GEN-LAST:event_cbInputTextItemStateChanged

    // Manually added, Feb. 2006
    private void cbLanguageItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) { // new item selected
            updateVoices();
            updateSampleTexts();
        }
    }

    private Vector readSampleTexts(Locale locale)
    {
        Vector texts = new Vector();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("sampletexts_"+locale.getLanguage()+".txt"),"UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(""))
                    texts.add(line);
            }
        } catch (IOException ioe) {}
        return texts;
    }

    private Locale getSelectedLanguage()
    {
        String displayLanguage = (String) cbLanguage.getSelectedItem();
        return (Locale) localeByDisplayLanguage.get(displayLanguage);
    }

    private void updateVoices()
    {
        Locale locale = getSelectedLanguage();
        cbVoice.removeAllItems();
        Vector voices = (Vector) voicesByLocale.get(locale);
        assert voices != null;
        for (Iterator it = voices.iterator(); it.hasNext(); ) {
            MaryClient.Voice v = (MaryClient.Voice) it.next();
            cbVoice.addItem(v);
            if (v.name().equals("de7") || v.name().equals("de6") || v.name().equals("us1")) {
                cbVoice.setSelectedItem(v);
            }
        }
    }
    
    private void updateSampleTexts()
    {
        Locale locale = getSelectedLanguage();
        cbInputText.removeAllItems();
        Vector texts = (Vector) sampleTextsByLocale.get(locale);
        assert texts != null;
        for (Iterator it = texts.iterator(); it.hasNext(); ) {
            String s = (String) it.next();
            cbInputText.addItem(s);
        }
        cbInputText.removeItemAt(0);
        cbInputText.setSelectedIndex(0);
    }

    private void showPowerMenuItemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showPowerMenuItemItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) { // new item selected
            ftPanel.setShowPower(true);
        } else { // deselected
            ftPanel.setShowPower(false);            
        }
            ftPanel.verifyPowerVisible();
            verifyPowerVisible();
    }//GEN-LAST:event_showPowerMenuItemItemStateChanged

    private void showMaryXMLMenuItemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showMaryXMLMenuItemItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) { // new item selected
            jScrollPane1.setVisible(true);
            lProsodyXML.setVisible(true);
        } else { // deselected
            jScrollPane1.setVisible(false);
            lProsodyXML.setVisible(false);
        }
    }//GEN-LAST:event_showMaryXMLMenuItemItemStateChanged

    private void cbVoiceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbVoiceItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) { // new item selected
            requestUpdateProsodyXML();
        }
    }//GEN-LAST:event_cbVoiceItemStateChanged

    private void cbInputTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbInputTextActionPerformed
        Object item = cbInputText.getSelectedItem();
        if (((DefaultComboBoxModel)cbInputText.getModel()).getIndexOf(item) == -1) {
            // new sentence typed in, remember it
            cbInputText.addItem(item);
        }
        requestUpdateProsodyXML();
    }//GEN-LAST:event_cbInputTextActionPerformed

    private void tfPowerValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPowerValueActionPerformed
    updatePowerModelFromTextFields();
    }//GEN-LAST:event_tfPowerValueActionPerformed

    private void tfEvaluationValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfEvaluationValueActionPerformed
        updateFeeltraceModelFromTextFields();
    }//GEN-LAST:event_tfEvaluationValueActionPerformed

    private void tfActivationValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfActivationValueFocusLost
        updateFeeltraceModelFromTextFields();
    }//GEN-LAST:event_tfActivationValueFocusLost

    private void tfPowerValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPowerValueFocusLost
        updatePowerModelFromTextFields();
    }//GEN-LAST:event_tfPowerValueFocusLost
    
    public void requestExit() {
        emoTransformer.requestExit();
        if (synthesiseAsynchronously)
            asynchronousSynthesiser.requestExit();
    }

    private synchronized void customInitComponents() 
    throws IOException, UnknownHostException {
        if (synthesiseAsynchronously) {
            if (maryServerHost == null) {
                asynchronousSynthesiser = new AsynchronousThreadedMaryClient(this);
            } else {
                asynchronousSynthesiser = new AsynchronousThreadedMaryClient
                    (this, maryServerHost, maryServerPort, false, false);
            }
            asynchronousSynthesiser.start();
        } else {
            if (maryServerHost == null) {
                synchronousSynthesiser = new MaryClient();
            } else {
                synchronousSynthesiser = new MaryClient(maryServerHost, maryServerPort, false, false);
            }
        }
        
        try {
            emoTransformer = new EmoTransformer(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        emoTransformer.start();
        
        verifyPowerVisible();
        feeltraceModel().addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                updateFeeltraceDisplays();
                requestUpdateProsodyXML();
            }
        });
        if (ftPanel.showPower()) {
            powerModel().addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    updatePowerDisplays();
                    requestUpdateProsodyXML();
                }
            });
        }

        Vector voiceInfo;
        if (synthesiseAsynchronously) {
            voiceInfo = asynchronousSynthesiser.getServerVoices();
        } else {
            voiceInfo = synchronousSynthesiser.getGeneralDomainVoices();
        }
        
        if (voiceInfo == null) {
            String host;
            int port;
            if (synthesiseAsynchronously) {
                host = asynchronousSynthesiser.getHost();
                port = asynchronousSynthesiser.getPort();
            } else {
                host = synchronousSynthesiser.getHost();
                port = synchronousSynthesiser.getPort();
            }
            JOptionPane.showMessageDialog(null, "Cannot start EmoSpeak: Server "+host+":"+port+" has no general domain voices!",
                    "No voices available", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        voicesByLocale = new HashMap();
        sampleTextsByLocale = new HashMap();
        localeByDisplayLanguage = new HashMap();
        for (Iterator it = voiceInfo.iterator(); it.hasNext(); ) {
            MaryClient.Voice v = (MaryClient.Voice) it.next();
            if (!voicesByLocale.containsKey(v.getLocale())) {
                voicesByLocale.put(v.getLocale(), new Vector());
            }
            Vector localeVoices = (Vector) voicesByLocale.get(v.getLocale());
            localeVoices.add(v);
            if (v.getLocale().equals(Locale.GERMAN)) haveGerman = true;
        }
                
        for (Iterator it = voicesByLocale.keySet().iterator(); it.hasNext(); ) {
            Locale locale = (Locale) it.next();
            sampleTextsByLocale.put(locale, readSampleTexts(locale));
            localeByDisplayLanguage.put(locale.getDisplayLanguage(), locale);
            cbLanguage.addItem(locale.getDisplayLanguage());
        }
        if (haveGerman) cbLanguage.setSelectedItem(Locale.GERMAN);
        updateVoices();
        updateSampleTexts();
    }
    
    private void updateFeeltraceModelFromTextFields() {
        boolean validA = false;
        boolean validE = false;
        int A = 0;
        int E = 0;
        try {
            A = Integer.parseInt(tfActivationValue.getText());
            if (feeltraceModel().getMinY() <= A && A <= feeltraceModel().getMaxY()) {
                validA = true;
            }
        } catch (NumberFormatException e) {}
        try {
            E = Integer.parseInt(tfEvaluationValue.getText());
            if (feeltraceModel().getMinX() <= E && E <= feeltraceModel().getMaxX()) {
                validE = true;
            }
        } catch (NumberFormatException e) {}
        int newA = validA ? A : feeltraceModel().getY();
        int newE = validE ? E : feeltraceModel().getX();
        feeltraceModel().setXY(newE, newA);
    }

    private void updatePowerModelFromTextFields() {
        boolean valid = false;
        int value = 0;
        try {
            value = Integer.parseInt(tfPowerValue.getText());
            if (value >= powerModel().getMinimum() &&
            value <= powerModel().getMaximum()) {
                valid = true;
            }
        } catch (NumberFormatException e) {}
        if (valid)
            // adopt new value
            powerModel().setValue(value);
        else
            // reset display to old value
            tfPowerValue.setText(String.valueOf(powerModel().getValue()));

    }

    
    protected void paintComponent(java.awt.Graphics graphics) {
        verifyPowerVisible();
        super.paintComponent(graphics);
    }
    
    private void verifyPowerVisible() {
        lPowerValue.setVisible(ftPanel.showPower());
        tfPowerValue.setVisible(ftPanel.showPower());
    }
    
    private void updateFeeltraceDisplays() {
        tfActivationValue.setText(String.valueOf(feeltraceModel().getY()));
        tfEvaluationValue.setText(String.valueOf(feeltraceModel().getX()));
    }
    
    private void updatePowerDisplays() {
        tfPowerValue.setText(String.valueOf(powerModel().getValue()));
    }
    
    private void requestUpdateProsodyXML() {
        Object selectedText = cbInputText.getSelectedItem();
        if (selectedText == null) return;
        String text = selectedText.toString();
        emoTransformer.setEmotionValues(feeltraceModel().getY(),
                feeltraceModel().getX(), powerModel().getValue(),
                text, getSelectedLanguage(), r++);
    }
    
    public synchronized void updateProsodyXML(String prosodyxmlString, int r1) {
        tpProsodyXML.setText(prosodyxmlString);
        if (synthesiseAsynchronously) {
			MaryClient.Voice maryClientVoice = (MaryClient.Voice) cbVoice.getSelectedItem();
			assert maryClientVoice != null; 
			asynchronousSynthesiser.scheduleRequest(prosodyxmlString, maryClientVoice, r1);
        }
    }
    
    private void preparePlayAudio() throws Exception {
        if (synthesiseAsynchronously) {
            if (nextAudio != null) {
                currentAudio = nextAudio;
                nextAudio = null;
            }
        } else { // synthesise now
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            synchronousSynthesiser.process(tpProsodyXML.getText(),
                    "RAWMARYXML",
                    "AUDIO",
                    "AU",
                    ((MaryClient.Voice) cbVoice.getSelectedItem()).name(),
                    os);
            byte[] bytes = os.toByteArray();
            currentAudio = AudioSystem.getAudioInputStream(new ByteArrayInputStream(bytes));
        }
        if (currentAudio == null && (clip == null || !clip.isOpen())) {
            bPlay.setEnabled(false);
            throw new Exception("No audio data to play (did synthesis succeed?)");
        }
        if (currentAudio != null) {
            if (clip != null && clip.isOpen()) closeClip();
            // Create new clip
            DataLine.Info info =
                new DataLine.Info(Clip.class, currentAudio.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(new LineListener() {
                public void update(LineEvent le) {
                    if (le.getType() == LineEvent.Type.STOP) {
                        closeAudio();
                    }
                }
            });
            clip.open(currentAudio);
            currentAudio.close();
            currentAudio = null;
        }
        // do this in particular for the previously played clips:
        clip.setFramePosition(0);
        // And now, clip.start() will play
    }
    
    private void playAudio() {
            assert clip != null && clip.isOpen();
            clip.start();
    }
    
    private void closeAudio() {
        bPlay.setText("Play");
    }
    
    private void closeClip() {
        // workaround for a bug in Linux-based Java VM from Sun:
        if (!(System.getProperty("java.vendor").equals("Sun Microsystems Inc.") &&
              System.getProperty("os.name").equals("Linux"))) {
            clip.close();
        }        
    }
    
    public void setNextAudio(javax.sound.sampled.AudioInputStream audioInputStream) {
        nextAudio = audioInputStream;
    }    

    
    
    
    
    
    
    
    private javax.swing.JTextField tfPowerValue;
    private javax.swing.JCheckBoxMenuItem showPowerMenuItem;
    private javax.swing.JComboBox cbInputText;
    private javax.swing.JLabel lPowerValue;
    private javax.swing.JButton bPlay;
    private javax.swing.JTextPane tpProsodyXML;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBoxMenuItem showMaryXMLMenuItem;
    private javax.swing.JLabel lActivationValue;
    private javax.swing.JPanel inputTextPanel;
    private javax.swing.JTextField tfActivationValue;
    private javax.swing.JLabel lVoice;
    private javax.swing.JLabel lProsodyXML;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JPanel dimensionValuePanel;
    private JFeeltracePanel ftPanel;
    private javax.swing.JComboBox cbVoice;
    private javax.swing.JPanel audioPanel;
    private javax.swing.JLabel lInputText;
    private javax.swing.JLabel lEvaluationValue;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTextField tfEvaluationValue;
    private JComboBox cbLanguage;    
}
