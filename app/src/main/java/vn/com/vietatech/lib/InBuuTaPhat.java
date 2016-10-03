package vn.com.vietatech.lib;

import com.citizen.jpos.command.ESCPOS;
import com.citizen.jpos.printer.ESCPOSPrinter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import vn.com.vietatech.dto.Delivery;

public class InBuuTaPhat {
    private ESCPOSPrinter posPtr;
    private final char ESC = ESCPOS.ESC;
    private final char LF = ESCPOS.LF;
    /*private final char K0 = 0x30;
    private final char K1 = 0x31;
    private final char K2 = 0x32;
    private final char K3 = 0x33;
    private final char K4 = 0x34;
    private final char K5 = 0x35;
    private final char K6 = 0x36;
    private final char K7 = 0x37;
    private final char K8 = 0x38;
    private final char K9 = 0x39;
    */
    byte[] m_fileData;
    File loadFile = null;
    int ROW =0;

    public InBuuTaPhat()
    {
        posPtr = new ESCPOSPrinter();
    }
    public Boolean writeSD(String fname, String fcontent){
        try {

            String fpath = "/sdcard/"+fname+".txt";

            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            //Log.d("Suceess","Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public byte[] readSDCard(String fname){

        File file = new File("/sdcard/"+fname+".txt");
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
        return bytes;

    }
    public String readSD(String fname){

        BufferedReader br = null;
        String response = null;

        try {

            StringBuffer output = new StringBuffer();
            String fpath = "/sdcard/"+fname+".txt";
            //String fpath = Environment.getExternalStorageDirectory().getPath()+fname+".txt";

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line +"\n");
            }
            response = output.toString();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
        return response;

    }
    public void ComamdFingering() throws UnsupportedEncodingException
    {

        posPtr.printNormal(ESC + "{"+"Q"+"U"+"I"+"T"+"}");

    }

    public void receipt(Delivery delivery) throws UnsupportedEncodingException
    {
        ROW = 0;
        String Sendtext = ResetPrinterCommand();
        Sendtext = Sendtext + FontSize("Times New Roman",8);// select font
        Sendtext = Sendtext + "nasc \"utf-8\"\r\n";
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 8);// feed paper
        ROW = ROW+15;
        Sendtext = Sendtext + "DIR 3\r\n";// rotate 180
        Sendtext = Sendtext + "ALIGN 4\r\n";
        Sendtext = Sendtext + GoPositionBold(385,  ROW, 9);
        ROW = ROW+25;
        Sendtext = Sendtext + "prtxt \"Tổng công ty bưu điện Việt Nam\"\r\n";
        Sendtext = Sendtext + "FONT \"Times New Roman Bold\",9\r\n";// Only Bold
        Sendtext = Sendtext + GoPositionBold(320,  ROW, 9);
        ROW = ROW+25;
        Sendtext = Sendtext + GoPositionBold(320,  ROW, 9);
        ROW = ROW+55;
        Sendtext = Sendtext + "prtxt \"BIỆN NHẬN\"\r\n";
        Sendtext = Sendtext + FontSize("Times New Roman", 9);
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 9);
        ROW = ROW+35;
        Sendtext = Sendtext + "prtxt \"MÃ VẠCH: " + delivery.getItemCode() + "\"\r\n";
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 9);
        ROW = ROW+35;
        Sendtext = Sendtext + "prtxt \"TÊN KH: " + delivery.getRealReciverName() + "\"\r\n";
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 9);
        ROW = ROW+35;
        Sendtext = Sendtext + "prtxt \"CMND: " + delivery.getRealReceiverIdentification() + "\"\r\n";
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 9);
        ROW = ROW+35;
        Sendtext = Sendtext + "prtxt \"Số tiền: " + delivery.getPrice() + "\"\r\n";
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 9);
        ROW = ROW+35;
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 9);
        ROW = ROW+25;
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 9);
        ROW = ROW+25;
        Sendtext = Sendtext + "BARSET \"EAN8\"\r\n";
        Sendtext = Sendtext + "BARFONT ON\r\n";
        Sendtext = Sendtext + "PRBAR \"1234567.12345\"\r\n";
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 9);

        Sendtext = Sendtext + "printfeed\r\n";
        Sendtext = Sendtext + GoPositionNormal(385,  ROW, 9);

        posPtr.printNormal(Sendtext);
    }
    public String ResetPrinterCommand()
    {
        return "CLEAR\r\n";
    }
    public String GoPositionNormal(int X,  int Y, int Size)
    {
        String Chuoi = "";
        if (Size == 8)
        {
            Chuoi = "PRPOS" + X + "," + (Y + 15)+ "\r\n";
            Y = Y + 15;
        }
        else if (Size == 9)
        {
            Chuoi = "PRPOS" + X + "," + (Y + 25) + "\r\n";
            Y = Y + 25;
        }
        else if (Size == 10)
        {
            Chuoi = "PRPOS" + X + "," + (Y + 30) + "\r\n";
            Y = Y + 30;
        }
        return Chuoi;
    }
    public String GoPositionBold(int X, int Y, int Size)
    {
        String Chuoi = "";
        if (Size == 8)
        {
            Chuoi = "PRPOS" + X + "," + (Y + 25) + "\r\n";
            Y = Y + 15;
        }
        else if (Size == 9)
        {
            Chuoi = "PRPOS" + X + "," + (Y + 35) + "\r\n";
            Y = Y + 25;
        }
        else if (Size == 10)
        {
            Chuoi = "PRPOS" + X + "," + (Y + 40) + "\r\n";
            Y = Y + 30;
        }
        return Chuoi;
    }
    public String FontSize(String FontName, int Size)
    {
        String Chuoi = "";
        Chuoi = "font \"" + FontName + "\"," + Size + "\r\n";
        return Chuoi;
    }


    public void print(Delivery delivery) throws UnsupportedEncodingException
    {
        ComamdFingering();
        receipt(delivery);
    }
}
