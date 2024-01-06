package com.example.teabreak_app.Ui;

import static com.example.teabreak_app.ModelClass.PermissionsChecker.REQUIRED_PERMISSION;
import static com.example.teabreak_app.ModelClass.PermissionsChecker.REQUIRED_PERMISSION132;
import static com.example.teabreak_app.Ui.PermissionsActivity.PERMISSION_REQUEST_CODE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.fonts.Font;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.teabreak_app.Adapter.Orderdetailsadapter;
import com.example.teabreak_app.ModelClass.FileUtils;
import com.example.teabreak_app.ModelClass.OrderdetailsModel;
import com.example.teabreak_app.ModelClass.PermissionsChecker;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityOrderdetailsBinding;
import com.google.android.datatransport.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Orderdetails extends AppCompatActivity {
    private ActivityOrderdetailsBinding binding;
    String order_no,Amount,order_date,delivery_mode,order_id;
    ProgressDialog progressDialog;
    private TeaBreakViewModel viewModel;
   Orderdetailsadapter orderdetailsadapter;

    ArrayList<OrderdetailsModel> list=new ArrayList<>();

    private PermissionsChecker checker;
    public static final String APPLICATION_ID = "com.example.teabreak_app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderdetailsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        checker=new PermissionsChecker(Orderdetails.this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        order_no=getIntent().getStringExtra("order_no");
        Amount=getIntent().getStringExtra("Amount");
        Log.d("amount",Amount);
        order_date=getIntent().getStringExtra("order_date");
        delivery_mode=getIntent().getStringExtra("delivery_mode");
        order_id=getIntent().getStringExtra("order_id");
        binding.orderNum.setText(order_no);
        binding.tvAmount.setText( "₹"+Amount);
        binding.tvOrderDate.setText(order_date);
        binding.tvVehicle.setText(delivery_mode);
        viewModel = ViewModelProviders.of(Orderdetails.this).get(TeaBreakViewModel.class);
        order_details_list_api_call();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvlist.setLayoutManager(linearLayoutManager);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orderdetails.this,Orders_List_Activity.class));
            }
        });

        binding.whatsappImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receipt();
            }
        });

        binding.llOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //createPdf(FileUtils.getAppPath(Orderdetails.this) +"9390126304"+"("+"12/19/2023"+")"+".pdf");
               // createPdf();
                receipt();
            }
        });

    }


    public void receipt(){
        SimpleDateFormat sdf1=new SimpleDateFormat("ddMMyyyyHHmmss");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if (checker.lacksPermissions(REQUIRED_PERMISSION132)) {
                Log.e("payment_success","permission_check_13");
                PermissionsActivity.startActivityForResult(Orderdetails.this, PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION132);
            } else {
                Log.e("payment_success","create pdf for payment success activity 13");

                String pdfFilePath = getExternalCacheDir().getPath();

                createPdf(FileUtils.getAppPath(Orderdetails.this) +"9390126304"+"("+sdf1.format(System.currentTimeMillis())+")"+".pdf");
              //  createPdf(pdfFilePath +"9390126304"+"("+sdf1.format(System.currentTimeMillis())+")"+".pdf");

            /*    File direc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                createPdf(direc.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis()) + ".pdf");
*/


              /*   File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                 createPdf(dir.getAbsolutePath().toString()+"/"+ sdf1.format(System.currentTimeMillis())+".pdf");*/

            }

        }else{
            if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
                Log.e("payment_success","permission_check");
                PermissionsActivity.startActivityForResult(Orderdetails.this, PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);
            } else {
                Log.e("payment_success","create pdf for payment success activity");
                String pdfFilePath = getExternalCacheDir().getPath();

                createPdf(FileUtils.getAppPath(Orderdetails.this) +"9390126304"+"("+sdf1.format(System.currentTimeMillis())+")"+".pdf");
              //  createPdf(pdfFilePath +"9390126304"+"("+sdf1.format(System.currentTimeMillis())+")"+".pdf");

             /*   File direc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                Log.e("direc",direc.toString());
                createPdf(direc.getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis()) + ".pdf");
*/

            }
        }

        String pdfFilePath = getExternalCacheDir().getPath();
        File outputFile = new File( FileUtils.getAppPath(Orderdetails.this)+"9390126304"+"("+sdf1.format(System.currentTimeMillis())+")"+".pdf");
       // File outputFile = new File( pdfFilePath+"9390126304"+"("+sdf1.format(System.currentTimeMillis())+")"+".pdf");
       // File outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Uri uri = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            uri= Uri.fromFile(outputFile);
            Log.e("file_path", String.valueOf(uri));
        } else {
            try {
                uri=FileProvider.getUriForFile(Orderdetails.this,
                        APPLICATION_ID+ ".fileprovider", outputFile);

            }catch (Exception e){
                Toast.makeText(Orderdetails.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e("file_exception","Exception"+e.getLocalizedMessage());
            }
        }
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String currentDate = sdf.format(System.currentTimeMillis());
        Calendar cal= Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM-yyyy");
        String month_name = month_date.format(cal.getTime());


        StringBuilder s;
        s = new StringBuilder("9390126304");

        for(int i = 5; i < s.length(); i += 6){
            s.insert(i, " ");
            Log.d("TAG","inserted "+i+" "+s);
        }
        Log.d("TAG","mobile "+s);
        String toNumber = "+91 "+s; // contains spaces.
        toNumber = toNumber.replace("+", "").replace(" ", "");
        Log.d("TAG","toNumber "+toNumber);
        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "");
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("image/png");
        startActivity(sendIntent);
            /*try {
                String text = "  Bill for : "+formattedDate1+
                        "\n------------------------\n"+
                        WsUrlConstants.SERVICES_name +"\n"+
                        ""+WsUrlConstants.SERVICE_MOBILE_NUMBER+ "\n" +
                        ""+WsUrlConstants.Adderss+"\n"+
                        "Date : " + currentDate +
                        "\n------------------------\n"+
                        "Cust.No       : "+customerNumber+
                        "\nName        : "+firstName+" "+lastName +
                        //"\n Name       : " +customerDataModel.getState()+
                        "\nArea        : " + custAddress +
                        "\nMobile      : " + customerDataModel.getMobile_no() +
                        "\nStart Date  : " + customerDataModel.getStart_date() +
                        "\nEnd Date    : " + customerDataModel.getEnd_date() +
                        "\n\nPrevious Due: Rs."+getroundedFigure(customerDataModel.getPrevious_due())+
                        // "\nFor month:"+formattedDate1+
                        "\n\nPackage Info : "+
                        "\nBasic Pack   : Rs." + basicpack +
                        "\nAla-carte price: Rs." + Alacarteprice +
                        "\nBoque price  : Rs." + boquetprice +
                        "\nSGST         : Rs." + String.valueOf(Sgst) +
                        "\nCGST         : Rs." + String.valueOf(Sgst) +
                        "\nMonthly Bill : Rs."+customerDataModel.getMonthlybill()+
                        "\nAmount Due   : Rs."+ customerDataModel.getPending_amount()+
                        "\n------------------------" +
                        "\nAmount paid : Rs." + customerPaymentSuccessModel.getAmount_paid()
                        +"\nPending Amt: Rs." + balance +
                        "\n------------------------\n" +
                        "       Receipt No : \n "+customerPaymentSuccessModel.getInvoice()+
                        "\n\n   " + employeeDataModel.getEmp_first_name()+ " "+employeeDataModel.getEmp_last_name()+"\n"+
                        //   "     "+employeeDataModel.getEmp_mobile_no()+"" +
                        "\n\n\n\n\n";// Replace with your message.
                String toNumber = "+917989638134"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
                //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                startActivity(intent);
            }
            catch (Exception e){
                Toast.makeText(mContext, "Whatsapp not installed for this user", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }*/
    }


    public void createPdf(String dest) {

        Log.e("payment_success","create pdf method enter");
        Log.e("path",dest);
        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        try {

            Document document=new Document();
            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.LETTER);
            document.addCreationDate();
            document.addAuthor("Android School");
            document.addCreator("Pratik Butani");


            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;

         /*   BaseFont urName = null;
            try {
                urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }*/

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 100));

            // Title Order Details...
            // Adding Title....
            Font mOrderDetailsTitleFont = null;

        /*    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                mOrderDetailsTitleFont = new Font("urName", mValueFontSize, BaseColor.BLACK);
            }
*/

            Chunk mOrderDetailsTitleChunk = new Chunk("TeaBreak");
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);

            document.add(mOrderDetailsTitleParagraph);
            document.add(new Paragraph(""));


            Font mOrdermobileFont = null;
           /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                mOrdermobileFont = new Font("urName", mHeadingFontSize, mColorAccent);
            }*/
            Chunk mOrdermobileChunk = new Chunk("9390126304");
            Paragraph mOrdermobileParagraph = new Paragraph(mOrdermobileChunk);
            mOrdermobileParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrdermobileParagraph);
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            // Fields of Order Details...
            // Adding Chunks for Title and value

            PdfPTable accounts1 = new PdfPTable(2);
            accounts1.setWidths(new float[] {4,8});
            accounts1.setWidthPercentage(100);
            accounts1.addCell(getBillRowCell("Name"));
            accounts1.addCell(getheaderCellR(""+SaveAppData.getLoginData().getLogin_username()));

            accounts1.addCell(getBillRowCell("Mobile"));
            accounts1.addCell(getheaderCellR(""+SaveAppData.getLoginData().getUser_mobile()));

            accounts1.addCell(getBillRowCell("Address"));
            accounts1.addCell(getheaderCellR(""+SaveAppData.getLoginData().getAddress()));

            accounts1.addCell(getBillRowCell("Order Date"));
            accounts1.addCell(getheaderCellR(""+order_date));

            accounts1.addCell(getBillRowCell("Order No"));
            accounts1.addCell(getheaderCellR(""+order_no));

            accounts1.addCell(getBillRowCell(""));
            accounts1.addCell(getheaderCellR(""));






            PdfPTable billTable1 = new PdfPTable(4); //one page contains 15 records
            billTable1.setWidthPercentage(100);
            billTable1.setWidths(new float[] { 5,2,2,2 });
            billTable1.addCell(getBillHeaderCell2("Item"));
            billTable1.addCell(getBillHeaderCell2("Quantity"));
            billTable1.addCell(getBillHeaderCell2("Price"));
            billTable1.addCell(getBillHeaderCell2("Amount"));


            document.add(accounts1);
            document.add(billTable1);
            for(int i=0;i<list.size();i++){

                PdfPTable billTable11 = new PdfPTable(4); //one page contains 15 records
                billTable11.setWidthPercentage(100);
                billTable11.setWidths(new float[] { 5,2,2,2 });
                int a=list.size()-1;

                if(i==Integer.valueOf(list.size()-1)){
                    billTable11.addCell(getBillHeaderCellF(list.get(i).getLine_item_name()));
                    billTable11.addCell(getBillHeaderCellF(list.get(i).getQuantity()));
                    billTable11.addCell(getBillHeaderCellF("₹"+list.get(i).getPrice()));
                    billTable11.addCell(getBillHeaderCellF("₹"+list.get(i).getSub_total_price()));
                }else{

                    billTable11.addCell(getBillHeaderCell(list.get(i).getLine_item_name()));
                    billTable11.addCell(getBillHeaderCell(list.get(i).getQuantity()));
                    billTable11.addCell(getBillHeaderCell("₹"+list.get(i).getPrice()));
                    billTable11.addCell(getBillHeaderCell("₹"+list.get(i).getSub_total_price()));
                }

                document.add(billTable11);

            }

            PdfPTable accounts11 = new PdfPTable(4);
            accounts11.setWidths(new float[] { 5,2,2,2 });
            accounts11.setWidthPercentage(100);

            accounts11.addCell(getAccountsCell2("Total Amount"));
            accounts11.addCell(getAccountsCell2(""));
            accounts11.addCell(getAccountsCell2(""));
            accounts11.addCell(getAccountsCell2("₹"+Amount));

            document.add(accounts11);

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidths(new float[] { 3,2 });
            accounts.setSpacingBefore(20.0f);
            accounts.setWidthPercentage(70);
           /* accounts.addCell(getAccountsCell("Collected By"));
            accounts.addCell(getAccountsCellR("sudha"+"sdfgh"));*/

            accounts.addCell(getAccountsCell(" "));
            accounts.addCell(getAccountsCellR(" "));

            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell("GENERATED BY DIGITALRUPAY"));

            document.add(accounts);
            document.add(validity);


/*
            PdfPTable billTable12 = new PdfPTable(2); //one page contains 15 records
            billTable12.setWidthPercentage(100);
            billTable12.setWidths(new float[] { 5,2 });
            billTable12.addCell(getBillHeaderCell("Total Alacarte Price"));
            billTable12.addCell(getBillHeaderCell("20"));

            PdfPTable billTable2 = new PdfPTable(2); //one page contains 15 records
            billTable2.setWidthPercentage(100);
            billTable2.setWidths(new float[] { 5,2 });
            billTable2.addCell(getBillHeaderCell("Total Boque Price"));
            billTable2.addCell(getBillHeaderCell("10"));

            PdfPTable billTable21 = new PdfPTable(2); //one page contains 15 records
            billTable21.setWidthPercentage(100);
            billTable21.setWidths(new float[] { 5,2 });
            billTable21.addCell(getBillHeaderCell("SGST"));
            billTable21.addCell(getBillHeaderCell(String.valueOf(30)));

            PdfPTable billTable22 = new PdfPTable(2); //one page contains 15 records
            billTable22.setWidthPercentage(100);
            billTable22.setWidths(new float[] { 5,2 });
            billTable22.addCell(getBillHeaderCell("CGST"));
            billTable22.addCell(getBillHeaderCell(String.valueOf(30)));

            PdfPTable billTable3 = new PdfPTable(2); //one page contains 15 records
            billTable3.setWidthPercentage(100);
            billTable3.setWidths(new float[] { 5,2 });
            billTable3.addCell(getBillHeaderCell("Monthly Rental"));
            billTable3.addCell(getBillHeaderCell("200"));

            PdfPTable billTable4 = new PdfPTable(2); //one page contains 15 records
            billTable4.setWidthPercentage(100);
            billTable4.setWidths(new float[] { 5,2 });
            billTable4.addCell(getBillHeaderCellF("Amount Due"));
            billTable4.addCell(getBillHeaderCellF("20"));

            PdfPTable billTable11 = new PdfPTable(2);
            billTable11.addCell(getBillRowCell(" "));
            billTable11.addCell(getBillRowCell(""));

            PdfPTable billTable5 = new PdfPTable(2); //one page contains 15 records
            billTable5.setWidthPercentage(100);
            billTable5.setWidths(new float[] { 5,2 });
            billTable5.setSpacingBefore(30.0f);
            billTable5.addCell(getBillHeaderCell("Amount paid"));
            billTable5.addCell(getBillHeaderCell("200"));

            PdfPTable billTable6 = new PdfPTable(2); //one page contains 15 records
            billTable6.setWidthPercentage(100);
            billTable6.setWidths(new float[] { 5,2 });
            billTable6.addCell(getBillHeaderCellF("Pending Amt"));
            billTable6.addCell(getBillHeaderCellF("30"));


            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidths(new float[] { 3,2 });
            accounts.setSpacingBefore(20.0f);
            accounts.setWidthPercentage(70);
            accounts.addCell(getAccountsCell("Collected By"));
            accounts.addCell(getAccountsCellR("sudha"+"sdfgh"));

            accounts.addCell(getAccountsCell(" "));
            accounts.addCell(getAccountsCellR(" "));

            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell("GENERATED BY DIGITALRUPAY"));*/


         //   document.add(accounts1);
           /* document.add(billTable11);
            document.add(billTable1);
            document.add(billTable21);
            document.add(billTable22);
            document.add(billTable12);
            document.add(billTable2);
            document.add(billTable3);
            document.add(billTable4);
            document.add(billTable5);
            document.add(billTable6);
            document.add(accounts);
            document.add(validity);*/


            Log.e("document",document.toString());


            document.close();

            Toast.makeText(Orderdetails.this, "Created... :)", Toast.LENGTH_SHORT).show();

            //FileUtils.openFile(mContext, new File(dest));

        } catch (IOException | DocumentException ie) {
            Toast.makeText(Orderdetails.this, "createPdf: Error " + ie.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.e("pdf_Error",ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(Orderdetails.this, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }



    private String getroundedFigure(String unitprice) {
        BigDecimal bill = new BigDecimal(unitprice);
        BigDecimal roundedBill = bill.setScale(2, RoundingMode.HALF_UP);
        return String.valueOf(roundedBill);
    }

    public static PdfPCell getValidityCell(String text) {
        FontSelector fs = new FontSelector();
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 14);
        font.setColor(BaseColor.BLUE);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorder(0);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        return cell;
    }

    public static PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setStyle(com.itextpdf.text.Font.BOLD);
        font.setColor(BaseColor.BLACK);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        // cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getAccountsCell(String text) {
        FontSelector fs = new FontSelector();
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPadding (5.0f);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        return cell;
    }

    public static PdfPCell getAccountsCell2(String text) {
        FontSelector fs = new FontSelector();
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setColor(BaseColor.BLACK.BLACK);
        font.setStyle(com.itextpdf.text.Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setPadding (5.0f);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        return cell;
    }
    public static PdfPCell getBillHeaderCellF(String text) {
        FontSelector fs = new FontSelector();
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setColor(BaseColor.BLACK);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (10.0f);
        cell.setPaddingRight(20.0f);
        return cell;
    }


    public static PdfPCell getBillRowCell(String text) {

        FontSelector fs = new FontSelector();
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_LEFT);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        return cell;
    }

    public static PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setColor(BaseColor.BLACK);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setPadding (10.0f);
        return cell;
    }

    public static PdfPCell getBillHeaderCell2(String text) {
        FontSelector fs = new FontSelector();
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setColor(BaseColor.BLACK);
        font.setStyle(com.itextpdf.text.Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setPadding (10.0f);
        return cell;
    }


    public static PdfPCell getheaderCellR(String text) {
        FontSelector fs = new FontSelector();
        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setStyle(com.itextpdf.text.Font.BOLD);
        font.setColor(BaseColor.BLACK);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_LEFT);
        cell.setPadding (5.0f);
        cell.setLeft(20.0f);
        //  cell.setPaddingRight(20.0f);
        return cell;
    }


  /*  private void createPdf() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(42);

        String text = "Hello, World";
        float x = 500;
        float y = 900;

        canvas.drawText(text, x, y, paint);
        document.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "example.pdf";
        File file = new File(downloadsDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Written Successfully!!!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("mylog", "Error while writing " + e.toString());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
*/


    private void order_details_list_api_call() {
        JsonObject object = new JsonObject();
        Log.d("orderdetailsapicall",object.toString());
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("order_id",order_id);
        viewModel.get_order_history(object).observe(Orderdetails.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null){
                    Log.d("ordersDetails","Text"+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");
                        Log.d("dataorderdetails",jsonArray.toString());
                        for(int i=0;i<jsonArray.length();i++){
                            Log.d("forloop","loop");
                            OrderdetailsModel orderdetailsModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<OrderdetailsModel>() {
                            }.getType());
                            list.add(orderdetailsModel);
                            Log.d("orderdetailslist",String.valueOf(list.size()));
                        }
                        Log.d("orderdetailslist2",String.valueOf(list.size()));
                        orderdetailsadapter=new Orderdetailsadapter(list, Orderdetails.this);
                        Log.d("Adapter",String.valueOf(jsonArray.length()));
                        binding.rvlist.setAdapter(orderdetailsadapter);
                        Log.d("recycleview",String.valueOf(jsonArray.length()));
                        orderdetailsadapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Toast.makeText(Orderdetails.this, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(Orderdetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}



