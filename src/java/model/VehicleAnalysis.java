
package model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.util.Calendar;
import web.AppListener;

public class VehicleAnalysis {

    private long rowId;
    private String vehicleImage;
    private String vehicleReport;
    private Date analysisDate;

    public static String getCreateStatement() {
        return "CREATE TABLE IF NOT EXISTS reportss(\n"
                + "rowId_user INTEGER(50) not null"
                + "    ,vehicle_image varchar(50) not null\n"
                + "    , vehicle_report varchar(20) not null\n"
                + "    , analysis_date datetime\n"
                + ")";
    }

    public VehicleAnalysis(long rowId, String vehicleImage, String vehicleReport, Date analysisDate) {
        this.rowId = rowId;
        this.vehicleImage = vehicleImage;
        this.vehicleReport = vehicleReport;
        this.analysisDate = analysisDate;
    }
    
    public static ArrayList<VehicleAnalysis> getHistory(HttpServletRequest req) throws Exception {
    ArrayList<VehicleAnalysis> list = new ArrayList<>();
    
    // Obtém conexão com o banco de dados
    Connection con = AppListener.getConnection();
    
    // Obtém a sessão do usuário e o ID do usuário logado
    HttpSession session = req.getSession(false);
    if (session == null) {
        throw new Exception("Usuário não está logado.");
    }
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        throw new Exception("Usuário não encontrado na sessão.");
    }
    Long rowIdUser = loggedUser.getRowId();

    // Prepara a consulta SQL para evitar SQL Injection
    String sql = "SELECT vehicle_image, vehicle_report, analysis_date, rowId_user FROM reportss WHERE rowId_user = ?";
    PreparedStatement stmt = con.prepareStatement(sql);
    stmt.setLong(1, rowIdUser);

    // Executa a consulta e processa os resultados
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
        long rowId = rs.getLong("rowId_user");
        String vehicle_image = rs.getString("vehicle_image");
        String vehicle_report = rs.getString("vehicle_report");
        Date analysis_date = rs.getTimestamp("analysis_date");
        list.add(new VehicleAnalysis(rowId, vehicle_image, vehicle_report, analysis_date));
    }
    
    // Fecha os recursos
    rs.close();
    stmt.close();
    con.close();

    return list;
}


    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getVehicleReport() {
        return vehicleReport;
    }

    public void setVehicleReport(String vehicleReport) {
        this.vehicleReport = vehicleReport;
    }

    public Date getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(Date analysisDate) {
        this.analysisDate = analysisDate;
    }
}
//    public static VehicleAnalysis getStay(long rowid) throws Exception {
//        VehicleAnalysis vs = null;
//        String SQL = "SELECT rowid, * FROM vehicles_stays";
//        Connection con = AppListener.getConnection();
//        PreparedStatement s = con.prepareStatement(SQL);
//        s.setLong(1, rowid);
//        ResultSet rs = s.executeQuery();
//        if (rs.next()) {
//            vs = new VehicleAnalysis(
//                    rs.getInt("rowid"),
//                     rs.getString("vehicle_image"),
//                     rs.getString("vehicle_report"),
//                     rs.getTimestamp("analysis_date")
//            );
//        }
//        rs.close();
//        s.close();
//        con.close();
//        return vs;
//    }

    //public static ArrayList<VehicleAnalysis> getList() throws Exception {
//        ArrayList<VehicleAnalysis> list = new ArrayList<>();
//        Connection con = AppListener.getConnection();
//        Statement s = con.createStatement();
//        ResultSet rs = s.executeQuery("SELECT rowid, * FROM vehicles_stays"
//                + " WHERE end_stay IS NULL");
//        while (rs.next()) {
//            VehicleAnalysis vs = new VehicleAnalysis(
//                    rs.getLong("rowid"),
//                     rs.getString("vehicle_model"),
//                     rs.getString("vehicle_color"),
//                     rs.getString("vehicle_plate"),
//                     rs.getTimestamp("begin_stay")
//            );
//            list.add(vs);
//        }
//        rs.close();
//        s.close();
//        con.close();
//        return list;
//    }

//    public static ArrayList<VehicleAnalysis> getHistoryList() throws Exception {
//        ArrayList<VehicleAnalysis> list = new ArrayList<>();
//        String SQL = "SELECT rowid, * FROM vehicles_reports";
//        Connection con = AppListener.getConnection();
//        PreparedStatement s = con.prepareStatement(SQL);
//        ResultSet rs = s.executeQuery();
//        while (rs.next()) {
//            VehicleAnalysis vs = new VehicleAnalysis(
//                    rs.getLong("rowid"),
//                     rs.getString("vehicle_image"),
//                     rs.getString("vehicle_report"),
//                     rs.getTimestamp("analysis_date")
//                    
//            );
//            list.add(vs);
//        }
//        rs.close();
//        s.close();
//        con.close();
//        return list;
//    }
//
////    public static void addVehicleStay(String image, String image_report)
////            throws Exception {
////        String SQL = "INSERT INTO vehicles_stays VALUES("
////                + "?" //vehicle_image
////                + ", ?" //vehicle_report
////                + ", ?" // analysis_date
////                + ")";
////        Connection con = AppListener.getConnection();
////        PreparedStatement s = con.prepareStatement(SQL);
////        s.setString(1, model);
////        s.setString(2, color);
////        s.setString(3, plate);
////        s.setTimestamp(4, new Timestamp(new Date().getTime()));
////        s.execute();
////        s.close();
////        con.close();
////    }
//
//    public static void finishVehicleStay(long rowid, double price)
//            throws Exception {
//        String SQL = "UPDATE vehicles_stays"
//                + " SET end_stay=?, price=?"
//                + " WHERE rowid =?";
//        Connection con = AppListener.getConnection();
//        PreparedStatement s = con.prepareStatement(SQL);
//        s.setTimestamp(1, new Timestamp(new Date().getTime()));
//        s.setDouble(2, price);
//        s.setLong(3, rowid);
//        s.execute();
//        s.close();
//        con.close();
//    }
//
//    public long getRowId() {
//        return rowId;
//    }
//
//    public void setRowId(long rowId) {
//        this.rowId = rowId;
//    }
//
//    public String getVehicleModel() {
//        return VehicleModel;
//    }
//
//    public void setVehicleModel(String VehicleModel) {
//        this.VehicleModel = VehicleModel;
//    }
//
//    public String getVehicleColor() {
//        return VehicleColor;
//    }
//
//    public void setVehicleColor(String VehicleColor) {
//        this.VehicleColor = VehicleColor;
//    }
//
//    public String getVehiclePlate() {
//        return VehiclePlate;
//    }
//
//    public void setVehiclePlate(String VehiclePlate) {
//        this.VehiclePlate = VehiclePlate;
//    }
//
//    public Date getBeginStay() {
//        return beginStay;
//    }
//
//    public void setBeginStay(Date beginStay) {
//        this.beginStay = beginStay;
//    }
//
//    public Date getEndStay() {
//        return endStay;
//    }
//
//    public void setEndStay(Date endStay) {
//        this.endStay = endStay;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public double getTotal() throws Exception {
//        Calendar begin = Calendar.getInstance();
//        begin.setTime(beginStay);
//        Calendar now = Calendar.getInstance();
//        int horas = now.get(Calendar.HOUR_OF_DAY) - begin.get(Calendar.HOUR_OF_DAY) + 1;
//       // return horas * HourPrice.getCurrentPrice();
//       return 0;
//    }
//}