package adompo.ayyash.behay;

/**
 * Created by Abdul Rizal Adompo on 9/18/2016.
 */
public class ConfigUmum {

    public static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    public static final String IP="administrator.behy.co";




//login
    public static final String LOGIN_URL = "http://"+IP+"/User/loginParticipant";

    //regiser
    public static String REGISTER = "http://"+IP+"/User/registerParticipant";


    //Messege
    public static String NEWMESSAGE = "http://"+IP+"/Message/insertMessage";
    public static String URL_INBOX = "http://"+IP+"/Message/getListInbox/";




    public static final String SAVE_AKTIFITAS_FISIK = "http://"+IP+"/User/setAktifitasFisik";



//    News
    public static final String URL_SHOW_News ="http://"+IP+"/News/get_all";
    public static final String URL_NEWS_DETAIL ="http://"+IP+"/News/getDetil/";


    // lemak
    public static final String   SAVE_LEMAK ="http://"+IP+"/User/setFatParticipant";

    // berat


    public static final String   SAVE_BERAT ="http://"+IP+"/User/setBerat";
    public static final String   GET_KONDISI_TUBUH ="http://"+IP+"/User/getKondisiTubuh/";
    public static final String   GET_MY_BEHY ="http://"+IP+"/User/getMyBehy/";

    public static final String GET_AKTIVITAS_FISIK="http://"+IP+"/User/getAktifitasFisik/";


    public static final String URL_DELETE_ACTIVITY="http://"+IP+"//User/deleteAktifitasFisik/";



    public static final String   SAVE_TINGGI ="http://"+IP+"/User/setTinggi";


 //misal saja aku uwooo

    public static final String   SAVE_TINGGI2 ="http://"+IP+"/User/setTinggi";



    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String LOGIN_SUCCESS = "success";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String AMBIL_NAMA = "nama";
    public static final String NIS_SHARED_PREF = "email";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String ID_KELAS = "id_kelas";


}