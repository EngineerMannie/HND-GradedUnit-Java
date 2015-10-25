/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turtletots01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Martin
 */
public class Enquiries extends javax.swing.JFrame implements ActionListener {
    
    //VARIABLES
    
    Connection conn;
    Statement stmnt;
    ResultSet res;
    int currentRow = 0;
    boolean cbxlist1set = false;
    boolean cbxlist2set = false;
    
    
    //create pool locations ArrayList
    protected ArrayList<String> locations = new ArrayList<>();
    

    /**
     * Creates new form enquiries
     */
    public Enquiries() {
        initComponents();
        DoConnect();
    }
    
    private void DoConnect(){
        try {
            
            // connect to the database
            String host = "jdbc:derby://localhost:1527/TurtleTots01";
            String uName = "RangeRover";
            String uPass = "EstateHound";
            conn = DriverManager.getConnection(host, uName, uPass);
            
            // execute some SQL and load the records into the result set
            stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "SELECT * FROM APP.ENQUIRIES";
            res = stmnt.executeQuery(sql);
            
            // move the cursor to the first record and get the data
            res.next();
            setButtons();
            getAndDisplayRecord();
            
            //close connections
            //res.close();
            //stmnt.close();
            //conn.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Enquiries.this, ex.getMessage());
        }
        
    }
    
    public void setButtons(){
        // switch off buttons 
        
        btnSaveNewRecord.setEnabled(false);
        btnCancelNewRecord.setEnabled(false);
        
    }
    
    public void getAndDisplayRecord(){
        try{
            // get the record information
            int id_col = res.getInt("ENQUIRY_ID");
            String enqid = Integer.toString(id_col);
            Date date_col = (Date) res.getObject("ENQ_DATE");
            // format enquiry date
            SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yy");
            String enqdate = dateformat.format(date_col);
            String enqrySource = res.getString("ENQ_SOURCE");
            String first = res.getString("ENQ_FNAME");
            String last = res.getString("ENQ_LNAME");
            String pupil = res.getString("PUPIL_NAME");
            String age = res.getString("PUPIL_AGE");
            String prefloc = res.getString("PREFFERED_LOCATION");
            //System.out.println(strloc);
            //create pool locations ArrayList
            ArrayList<String> poollocs = new ArrayList<>();
            Statement stmnt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
            String sqlloc = "SELECT TOWN FROM APP.POOLS";
            ResultSet resloc = stmnt2.executeQuery(sqlloc);
            resloc.last();
            int locsize = resloc.getRow();
            resloc.first();
            //System.out.println("Before The Loop, Size = " + locsize);
            if (!cbxlist1set){
                for (int i=0; i<locsize; i++){
                    poollocs.add(resloc.getString("TOWN"));
                    resloc.next();
                }
                cbxlist1set = true;
            }
            String swimability = res.getString("SWIM_ABILITY");
            // create swim level ArrayList
            ArrayList<String> swimlevel = new ArrayList<>();
            Statement stmnt3 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
            String sqlslev = "SELECT SWIM_LEVEL FROM APP.SWIM_LEVELS";
            ResultSet resslev = stmnt3.executeQuery(sqlslev);
            resslev.last();
            int slevsize = resslev.getRow();
            resslev.first();
            //System.out.println("Before The Loop, Size = " + slevsize);
            if (!cbxlist2set){
                for (int i=0; i<slevsize; i++){
                    swimlevel.add(resslev.getString("SWIM_LEVEL"));
                    resslev.next();
                }
                cbxlist2set = true;
            }
            String email = res.getString("ENQ_EMAIL");
            String mnphone = res.getString("MAIN_PHONE_NO");
            String othphone = res.getString("OTHER_PHONE_NO");
            String address = res.getString("ADDRESS");
            String town = res.getString("TOWN");
            String county = res.getString("COUNTY");
            String postcode = res.getString("POSTCODE");
            String infoemail = res.getString("INFO_EMAILED");
            String bookletter = res.getString("BOOKING_LETTER");
            String termsconds = res.getString("TERMS_CONDITIONS");
            String marketperm = res.getString("MARKETING_PERMISSION");
            String confrecvd = res.getString("CONFIRMATION_RECIEVED");
            String bookmade = res.getString("BOOKING_MADE");
            int client_col = res.getInt("CLIENT_ID");
            String clientid = Integer.toString(client_col);
            // display the first record in the form text fields
            txtEnquiryID.setText(enqid);
            txtEnquiryDate.setText(enqdate);
            // combo hardcoded source selected?? if not??
            cbxEnqrySource.setSelectedItem(enqrySource);
            txtFirstName.setText(first);
            txtLastName.setText(last);
            txtMobileNo.setText(mnphone);
            txtPhoneNo.setText(othphone);
            txtEmail.setText(email);
            txtChildsName.setText(pupil);
            txtAge.setText(age);
            // combo pool locations list with selected item shown
            for (int i = 0; i < poollocs.size(); i++) {
                cbxPoolLocations.addItem(poollocs.get(i));
            }
            if (!(prefloc.equalsIgnoreCase("Not Selected"))){
                cbxPoolLocations.setSelectedItem(prefloc);
                
            } else {
                cbxPoolLocations.setSelectedItem("Not Selected");
            }
            // combo swim level list with selected item shown
            for (int i = 0; i < swimlevel.size(); i++) {
                cbxSwimLevels.addItem(swimlevel.get(i));
            }
            if (!(swimability.equalsIgnoreCase("Not Selected"))){
                cbxSwimLevels.setSelectedItem(swimability);
            } else {
                cbxSwimLevels.setSelectedItem("Not Selected");
            }
            txtAddress.setText(address);
            txtTown.setText(town);
            txtCounty.setText(county);
            txtPostCode.setText(postcode);
            // set the check boxes
            if (infoemail.equalsIgnoreCase("0")){
                chkInfoEmailed.setSelected(false);
            } else {
                chkInfoEmailed.setSelected(true);
            }
            if (bookletter.equalsIgnoreCase("0")){
                chkBookingLetter.setSelected(false);
            } else {
                chkBookingLetter.setSelected(true);
            }
            if (termsconds.equalsIgnoreCase("0")){
                chkTermsConditions.setSelected(false);
            } else {
                chkTermsConditions.setSelected(true);
            }
            if (marketperm.equalsIgnoreCase("0")){
                chkMarketingPermission.setSelected(false);
            } else {
                chkMarketingPermission.setSelected(true);
            }
            if (confrecvd.equalsIgnoreCase("0")){
                chkConfirmationReceived.setSelected(false);
            } else {
                chkConfirmationReceived.setSelected(true);
            }
            if (bookmade.equalsIgnoreCase("0")){
                chkBookingMade.setSelected(false);
            } else {
                chkBookingMade.setSelected(true);
            }
        }
        catch(SQLException ex) {
            JOptionPane.showMessageDialog(Enquiries.this, ex.getMessage());
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

        jPanel2 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        cbxEnqrySource = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        txtEnquiryDate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtEnquiryID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        txtAge = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtChildsName = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtPhoneNo = new javax.swing.JTextField();
        txtMobileNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        txtFirstName = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        cbxSwimLevels = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        cbxPoolLocations = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtPostCode = new javax.swing.JTextField();
        txtCounty = new javax.swing.JTextField();
        txtTown = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        chkConfirmationReceived = new javax.swing.JCheckBox();
        chkMarketingPermission = new javax.swing.JCheckBox();
        chkTermsConditions = new javax.swing.JCheckBox();
        jSeparator6 = new javax.swing.JSeparator();
        chkBookingMade = new javax.swing.JCheckBox();
        chkInfoEmailed = new javax.swing.JCheckBox();
        chkBookingLetter = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        btnDeleteRecord = new javax.swing.JButton();
        btnUpdateRecord = new javax.swing.JButton();
        btnNewRecord = new javax.swing.JButton();
        btnSaveNewRecord = new javax.swing.JButton();
        btnCancelNewRecord = new javax.swing.JButton();
        btnClientBooking = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        txtSearchLname = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtClientID = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(520, 735));

        jPanel2.setMinimumSize(new java.awt.Dimension(520, 730));
        jPanel2.setPreferredSize(new java.awt.Dimension(520, 730));

        cbxEnqrySource.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not Selected", "Magazine", "Newspaper", "Search Engine", "Social Media", "Swimmiing Pool", "Website", "Word of Mouth" }));
        cbxEnqrySource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxEnqrySourceActionPerformed(evt);
            }
        });

        jLabel14.setText("Source");

        txtEnquiryDate.setText("enquiry_date");
        txtEnquiryDate.setMinimumSize(new java.awt.Dimension(6, 24));
        txtEnquiryDate.setPreferredSize(new java.awt.Dimension(70, 24));
        txtEnquiryDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEnquiryDateActionPerformed(evt);
            }
        });

        jLabel8.setText("Date");

        jLabel11.setText("Child's Name");

        txtEnquiryID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtEnquiryID.setText("enquiry_id");

        jLabel4.setText("Email");

        jLabel2.setText("Mobile No.");

        jLabel9.setText("Enquiry By");

        txtAge.setText("age");

        jLabel15.setText("Age");

        txtChildsName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtChildsName.setText("childs_name");

        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtEmail.setText("email");

        txtPhoneNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPhoneNo.setText("phone_no");

        txtMobileNo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMobileNo.setText("mobile_no");

        jLabel3.setText("Phone No.");

        txtLastName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtLastName.setText("last_name");

        txtFirstName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtFirstName.setText("first_name");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txtChildsName, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtEmail)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                            .addComponent(txtMobileNo))
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtLastName)))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtChildsName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel17.setText("EnqryID");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtEnquiryID, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEnquiryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxEnqrySource, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEnquiryDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEnquiryID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxEnqrySource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)
                        .addComponent(jLabel8)
                        .addComponent(jLabel17)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtEnquiryDate, txtEnquiryID});

        cbxSwimLevels.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not Selected" }));
        cbxSwimLevels.setToolTipText("");

        jLabel12.setText("Swim Level");

        cbxPoolLocations.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not Selected" }));

        jLabel13.setText("Prefered location");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxPoolLocations, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxSwimLevels, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxSwimLevels, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(cbxPoolLocations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel10.setText("PostCode");

        jLabel6.setText("Town");

        jLabel7.setText("County");

        jLabel5.setText("Address");

        txtPostCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPostCode.setText("postcode");

        txtCounty.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCounty.setText("county");

        txtTown.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTown.setText("town");

        txtAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtAddress.setText("address");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCounty)
                    .addComponent(txtTown)
                    .addComponent(txtAddress)
                    .addComponent(txtPostCode, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCounty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtPostCode, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        chkConfirmationReceived.setText("Confirmation Received");

        chkMarketingPermission.setText("Marketing Permission Given");

        chkTermsConditions.setText("Terms & Conditions Sent");

        chkBookingMade.setText("Booking Made");

        chkInfoEmailed.setText("Information Emailed");
        chkInfoEmailed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkInfoEmailedActionPerformed(evt);
            }
        });

        chkBookingLetter.setText("Booking Letter Sent");

        btnDeleteRecord.setText("Delete Record");
        btnDeleteRecord.setMaximumSize(new java.awt.Dimension(117, 23));
        btnDeleteRecord.setMinimumSize(new java.awt.Dimension(117, 23));
        btnDeleteRecord.setPreferredSize(new java.awt.Dimension(105, 23));
        btnDeleteRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteRecordActionPerformed(evt);
            }
        });

        btnUpdateRecord.setText("Update Record");
        btnUpdateRecord.setMaximumSize(new java.awt.Dimension(117, 23));
        btnUpdateRecord.setMinimumSize(new java.awt.Dimension(117, 23));
        btnUpdateRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateRecordActionPerformed(evt);
            }
        });

        btnNewRecord.setText("New Record");
        btnNewRecord.setMaximumSize(new java.awt.Dimension(117, 23));
        btnNewRecord.setMinimumSize(new java.awt.Dimension(117, 23));
        btnNewRecord.setPreferredSize(new java.awt.Dimension(105, 23));
        btnNewRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewRecordActionPerformed(evt);
            }
        });

        btnSaveNewRecord.setText("Save New Record");
        btnSaveNewRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveNewRecordActionPerformed(evt);
            }
        });

        btnCancelNewRecord.setText("Cancel New Record");
        btnCancelNewRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelNewRecordActionPerformed(evt);
            }
        });

        btnClientBooking.setText("Generate Client Booking");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnUpdateRecord, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(btnNewRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDeleteRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSaveNewRecord))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelNewRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClientBooking, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDeleteRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClientBooking, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdateRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNewRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSaveNewRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelNewRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chkBookingLetter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chkTermsConditions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chkInfoEmailed, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chkMarketingPermission, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chkConfirmationReceived, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chkBookingMade, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jSeparator6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkInfoEmailed, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkMarketingPermission))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkBookingLetter)
                    .addComponent(chkConfirmationReceived))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkTermsConditions)
                    .addComponent(chkBookingMade))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setMaximumSize(new java.awt.Dimension(460, 95));
        jPanel5.setMinimumSize(new java.awt.Dimension(460, 95));
        jPanel5.setPreferredSize(new java.awt.Dimension(460, 95));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Enquiry");

        btnFirst.setText("First");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrevious.setText("Previous");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setText("Last");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        txtSearchLname.setText("last_name");
        txtSearchLname.setMinimumSize(new java.awt.Dimension(6, 30));
        txtSearchLname.setPreferredSize(new java.awt.Dimension(55, 30));
        txtSearchLname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchLnameActionPerformed(evt);
            }
        });

        jLabel16.setText("Search Surname");

        txtClientID.setText("ClientID");
        txtClientID.setMinimumSize(new java.awt.Dimension(6, 30));
        txtClientID.setPreferredSize(new java.awt.Dimension(56, 30));

        jLabel18.setText("ClientID");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtClientID, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchLname, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 19, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtClientID, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18)
                        .addComponent(jLabel16)
                        .addComponent(txtSearchLname, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSearch))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNext)
                            .addComponent(btnFirst)
                            .addComponent(btnLast)
                            .addComponent(btnPrevious, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29)))
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 687, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        try{
            res.first();
            getAndDisplayRecord();
        }
        catch (SQLException err){
             JOptionPane.showMessageDialog(Enquiries.this,err.getMessage());
        }
    }//GEN-LAST:event_btnFirstActionPerformed

    private void chkInfoEmailedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkInfoEmailedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkInfoEmailedActionPerformed

    private void txtSearchLnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchLnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchLnameActionPerformed

    private void btnCancelNewRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelNewRecordActionPerformed
        // TODO add your handling code here:
        try{
            res.absolute(currentRow);
            
            // switch the button functions off/on
            btnFirst.setEnabled(true);
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(true);
            btnLast.setEnabled(true);
            btnUpdateRecord.setEnabled(true);
            btnDeleteRecord.setEnabled(true);
            btnClientBooking.setEnabled(true);
            btnNewRecord.setEnabled(true);
            btnSearch.setEnabled(true);
            txtClientID.setEnabled(true);
            txtSearchLname.setEnabled(true);
            
            btnSaveNewRecord.setEnabled(false);
            btnCancelNewRecord.setEnabled(false);
            
            getAndDisplayRecord();
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
        }
        
    }//GEN-LAST:event_btnCancelNewRecordActionPerformed
    
    private void btnSaveNewRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveNewRecordActionPerformed
            
        // TODO add your handling code here:
        // get the user input from the textboxes
        String enqdate = txtEnquiryDate.getText();
        TtotsDates ttd = new TtotsDates();
        Date enqrydate = new Date((ttd.getYear(enqdate)),
                (ttd.getMonth(enqdate)),(ttd.getDay(enqdate)));
        String enqsource = (String) cbxEnqrySource.getSelectedItem();
        String first = txtFirstName.getText();
        String last = txtLastName.getText();
        String mnphone = txtMobileNo.getText();
        String othphone = txtPhoneNo.getText();
        String email = txtEmail.getText();
        String pupil = txtChildsName.getText();
        String age = txtAge.getText();
        String preflocation = (String) cbxPoolLocations.getSelectedItem();
        String swimlevel = (String) cbxSwimLevels.getSelectedItem();
        String address = txtAddress.getText();
        String town = txtTown.getText();
        String county = txtCounty.getText();
        String postcode = txtPostCode.getText();
        int infoemailed = 0;
        if (chkInfoEmailed.isSelected()) {infoemailed = 1;}
        int bookletter = 0;
        if (chkBookingLetter.isSelected()) {bookletter = 1;}
        int termsconditions = 0;
        if (chkTermsConditions.isSelected()) {termsconditions = 1;}
        int marketing = 0;
        if (chkMarketingPermission.isSelected()) {marketing = 1;}
        int confirmrecvd = 0;
        if (chkConfirmationReceived.isSelected()) {confirmrecvd = 1;}
        int bookmade = 0;
        if (chkBookingMade.isSelected()) {bookmade = 1;}

        // insert new record info
        try {
            res.moveToInsertRow();

            res.updateDate("ENQ_DATE", enqrydate);
            res.updateString("ENQ_SOURCE", enqsource);
            res.updateString("ENQ_FNAME", first);
            res.updateString("ENQ_LNAME", last);
            res.updateString("MAIN_PHONE_NO", mnphone);
            res.updateString("OTHER_PHONE_NO", othphone);
            res.updateString("ENQ_EMAIL", email);
            res.updateString("PUPIL_NAME", pupil);
            res.updateString("PUPIL_AGE", age);
            res.updateString("PREFFERED_LOCATION", preflocation);
            res.updateString("SWIM_ABILITY", swimlevel);
            res.updateString("ADDRESS", address);
            res.updateString("TOWN", town);
            res.updateString("COUNTY", county);
            res.updateString("POSTCODE", postcode);
            res.updateInt("INFO_EMAILED", infoemailed);
            res.updateInt("BOOKING_LETTER", bookletter);
            res.updateInt("TERMS_CONDITIONS", termsconditions);
            res.updateInt("MARKETING_PERMISSION", marketing);
            res.updateInt("CONFIRMATION_RECIEVED", confirmrecvd);
            res.updateInt("BOOKING_MADE", bookmade);

            res.insertRow();

            stmnt.close();
            res.close();

            // execute some SQL and load the records into the result set
            stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "SELECT * FROM APP.ENQUIRIES";
            res = stmnt.executeQuery(sql);
            res.last();

            getAndDisplayRecord();

            // switch the button functions off/on
            btnFirst.setEnabled(true);
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(true);
            btnLast.setEnabled(true);
            btnUpdateRecord.setEnabled(true);
            btnDeleteRecord.setEnabled(true);
            btnClientBooking.setEnabled(true);
            btnNewRecord.setEnabled(true);
            btnSearch.setEnabled(true);
            txtClientID.setEnabled(true);
            txtSearchLname.setEnabled(true);

            btnSaveNewRecord.setEnabled(false);
            btnCancelNewRecord.setEnabled(false);
                
        } 
        catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }//GEN-LAST:event_btnSaveNewRecordActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        try{
            if(res.previous()){
                getAndDisplayRecord();
            } 
            else {
                res.next();
                JOptionPane.showMessageDialog(Enquiries.this, "Beginning of File");
            }
        }
        catch (SQLException err){
             JOptionPane.showMessageDialog(Enquiries.this,err.getMessage());
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        try{
            if(res.next()){
                getAndDisplayRecord();
            } 
            else {
                res.previous();
                JOptionPane.showMessageDialog(Enquiries.this, "End of File");
            }
        }
        catch(SQLException err){
             JOptionPane.showMessageDialog(Enquiries.this,err.getMessage());
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        try{
            res.last();
            getAndDisplayRecord();
        }
        catch (SQLException err){
             JOptionPane.showMessageDialog(Enquiries.this,err.getMessage());
        }
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
         try {
            currentRow = res.getRow();
            String searchName = txtSearchLname.getText();
            
            // execute some SQL and load the records into the result set
            PreparedStatement pstmnt = null;
            String sql = ("SELECT * FROM APP.ENQUIRIES WHERE ENQ_LNAME = ?");
            pstmnt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmnt.setString(1, searchName);
            res = pstmnt.executeQuery();
            
            if (res.next()){
                getAndDisplayRecord();
            } else {
                JOptionPane.showMessageDialog(null, "No Name Found!");
                txtSearchLname.setText("Enter Surname");
                // execute some SQL and load the records into the result set
                stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String sql2 = "SELECT * FROM APP.ENQUIRIES";
                res = stmnt.executeQuery(sql2);
                res.absolute(currentRow);
                getAndDisplayRecord();
            }
        } catch (SQLException err) {
            JOptionPane.showMessageDialog(Enquiries.this, err.getMessage());
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtEnquiryDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEnquiryDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEnquiryDateActionPerformed

    private void cbxEnqrySourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxEnqrySourceActionPerformed
        // TODO add your handling code here:
        // cbxEnqrySource.addActionListener(this);
        // String enqrySource = (String)cbxEnqrySource.getSelectedItem();
        
    }//GEN-LAST:event_cbxEnqrySourceActionPerformed

    private void btnUpdateRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateRecordActionPerformed
        // TODO add your handling code here:
        // get the record values to update
        String enqdate = txtEnquiryDate.getText();
        TtotsDates ttd = new TtotsDates();
        Date enqrydate = new Date((ttd.getYear(enqdate)),
                (ttd.getMonth(enqdate)),(ttd.getDay(enqdate)));
        String enqsource = (String) cbxEnqrySource.getSelectedItem();
        String first = txtFirstName.getText();
        String last = txtLastName.getText();
        String mnphone = txtMobileNo.getText();
        String othphone = txtPhoneNo.getText();
        String email = txtEmail.getText();
        String pupil = txtChildsName.getText();
        String age = txtAge.getText();
        String preflocation = (String) cbxPoolLocations.getSelectedItem();
        String swimlevel = (String) cbxSwimLevels.getSelectedItem();
        String address = txtAddress.getText();
        String town = txtTown.getText();
        String county = txtCounty.getText();
        String postcode = txtPostCode.getText();
        int infoemailed = 0;
        if (chkInfoEmailed.isSelected()) {infoemailed = 1;}
        int bookletter = 0;
        if (chkBookingLetter.isSelected()) {bookletter = 1;}
        int termsconditions = 0;
        if (chkTermsConditions.isSelected()) {termsconditions = 1;}
        int marketing = 0;
        if (chkMarketingPermission.isSelected()) {marketing = 1;}
        int confirmrecvd = 0;
        if (chkConfirmationReceived.isSelected()) {confirmrecvd = 1;}
        int bookmade = 0;
        if (chkBookingMade.isSelected()) {bookmade = 1;}
        
        
           // update this record info
        try {
            res.updateDate("ENQ_DATE", enqrydate);
            res.updateString("ENQ_SOURCE", enqsource);
            res.updateString("ENQ_FNAME", first);
            res.updateString("ENQ_LNAME", last);
            res.updateString("MAIN_PHONE_NO", mnphone);
            res.updateString("OTHER_PHONE_NO", othphone);
            res.updateString("ENQ_EMAIL", email);
            res.updateString("PUPIL_NAME", pupil);
            res.updateString("PUPIL_AGE", age);
            res.updateString("PREFFERED_LOCATION", preflocation);
            res.updateString("SWIM_ABILITY", swimlevel);
            res.updateString("ADDRESS", address);
            res.updateString("TOWN", town);
            res.updateString("COUNTY", county);
            res.updateString("POSTCODE", postcode);
            res.updateInt("INFO_EMAILED", infoemailed);
            res.updateInt("BOOKING_LETTER", bookletter);
            res.updateInt("TERMS_CONDITIONS", termsconditions);
            res.updateInt("MARKETING_PERMISSION", marketing);
            res.updateInt("CONFIRMATION_RECIEVED", confirmrecvd);
            res.updateInt("BOOKING_MADE", bookmade);
            
            res.updateRow(); 
            JOptionPane.showMessageDialog(Enquiries.this, "Record Updated");
            
         } 
        catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }//GEN-LAST:event_btnUpdateRecordActionPerformed

    private void btnNewRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewRecordActionPerformed
        // TODO add your handling code here:
        try{
            currentRow = res.getRow();
            // move to end record, get end ID, add 1
            res.last();
            int id_new = (res.getInt("ENQUIRY_ID")+1);
            String idNew = Integer.toString(id_new);
            res.absolute(currentRow);
            
            // clear text fields
            txtEnquiryID.setText(idNew);
            txtEnquiryID.setFocusable(false);
            // get todays date
//            SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yy");
//            Calendar enqdate = Calendar.getInstance();
//            String enqrydate = dateformat.format(enqdate);
//            System.out.println(enqrydate);
//            txtEnquiryDate.setText(enqrydate);
//            txtEnquiryDate.setFocusable(false);
            txtEnquiryDate.selectAll();
            cbxEnqrySource.setSelectedItem("Not Selected");
            txtFirstName.setText("First Name");
            txtFirstName.selectAll();
            txtLastName.setText("Last Name");
            txtLastName.selectAll();
            txtMobileNo.setText("Mobile No.");
            txtMobileNo.selectAll();
            txtPhoneNo.setText("Phone No.");
            txtPhoneNo.selectAll();
            txtEmail.setText("email");
            txtEmail.selectAll();
            txtChildsName.setText("Child's Name");
            txtChildsName.selectAll();
            txtAge.setText("Age");
            cbxPoolLocations.setSelectedItem("Not Selected");
            cbxSwimLevels.setSelectedItem("Not Selected");
            txtAge.selectAll();
            txtAddress.setText("Address");
            txtAddress.selectAll();
            txtTown.setText("Town");
            txtTown.selectAll();
            txtCounty.setText("County");
            txtCounty.selectAll();
            txtPostCode.setText("Post Code");
            txtPostCode.selectAll();
            chkInfoEmailed.setSelected(false);
            chkBookingLetter.setSelected(false);
            chkTermsConditions.setSelected(false);
            chkMarketingPermission.setSelected(false);
            chkConfirmationReceived.setSelected(false);
            chkBookingMade.setSelected(false);
            
            // switch the button functions off/on
            btnFirst.setEnabled(false);
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);
            btnUpdateRecord.setEnabled(false);
            btnDeleteRecord.setEnabled(false);
            btnClientBooking.setEnabled(false);
            btnNewRecord.setEnabled(false);
            btnSearch.setEnabled(false);
            txtClientID.setEnabled(false);
            txtSearchLname.setEnabled(false);

            btnSaveNewRecord.setEnabled(true);
            btnCancelNewRecord.setEnabled(true);
        }
        catch(SQLException err){
            System.out.println(err.getMessage());
        }
        
    }//GEN-LAST:event_btnNewRecordActionPerformed

    private void btnDeleteRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteRecordActionPerformed
        // TODO add your handling code here:
        try{
            res.deleteRow();
            stmnt.close();
            res.close();
            // execute some SQL and load the records into the result set
            stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "SELECT * FROM APP.ENQUIRIES";
            res = stmnt.executeQuery(sql);
            
            res.next();
            getAndDisplayRecord();
            JOptionPane.showMessageDialog(Enquiries.this, "Record Deleted");
        
        } catch(SQLException err) {
            JOptionPane.showMessageDialog(Enquiries.this, err.getMessage());
        }
    }//GEN-LAST:event_btnDeleteRecordActionPerformed
    

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Enquiries.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Enquiries().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelNewRecord;
    private javax.swing.JButton btnClientBooking;
    private javax.swing.JButton btnDeleteRecord;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNewRecord;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnSaveNewRecord;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdateRecord;
    private javax.swing.JComboBox cbxEnqrySource;
    private javax.swing.JComboBox cbxPoolLocations;
    private javax.swing.JComboBox cbxSwimLevels;
    private javax.swing.JCheckBox chkBookingLetter;
    private javax.swing.JCheckBox chkBookingMade;
    private javax.swing.JCheckBox chkConfirmationReceived;
    private javax.swing.JCheckBox chkInfoEmailed;
    private javax.swing.JCheckBox chkMarketingPermission;
    private javax.swing.JCheckBox chkTermsConditions;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAge;
    private javax.swing.JTextField txtChildsName;
    private javax.swing.JTextField txtClientID;
    private javax.swing.JTextField txtCounty;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEnquiryDate;
    private javax.swing.JTextField txtEnquiryID;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtMobileNo;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextField txtPostCode;
    private javax.swing.JTextField txtSearchLname;
    private javax.swing.JTextField txtTown;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
