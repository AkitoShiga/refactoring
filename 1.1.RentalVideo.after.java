import java.util.*;
/*
 * ビデオのレンタル料金を計算して計算書を印刷する
 * 入力 どの映画 借りる日数
 * 出力 映画の分類を判定(一般、子供向け、新作)
 * 新作かどうかによってレンタルポイントもつく
 */

/*
 * statement が長すぎる
 *    switch を別の関数にする
 *    switch の中のローカル変数を一時変数かパラメーターにならないか検討する
 *        each -> 値が変化しないのでできる
 *        thisAmount -> 変化するがswitchの中だけ & 1つだけなので戻り値にする
 */
// 料金クラスを追加
abstract class Price {
    abstract int getPriceCode();

    abstract double getCharge(int daysRented);

    int getFrequentRenterPoints(int daysRented) {
        return 1;
    }
}

class ChildrenPrice extends Price {
    int getPriceCode() {
        return Movie.CHILDRENS;
    }
    double getCharge(int daysRented) {
        double result = 1.5;
        if (daysRented > 3)
            result += (daysRented -3) * 1.5;

        return result;
    }

}

class NewReleasePrice extends Price {
    int getPriceCode() {
        return Movie.NEW_RELEASE;
    }
    double getCharge(int daysRented) {
        return daysRented * 3;
    }
    int getFrequentRenterPoints(int daysRented) {
        return (daysRented > 1)? 2 : 1;
    }
}

class RegularPrice extends Price {
    int getPriceCode() {
        return Movie.REGULAR;
    }
    double getCharge(int daysRented) {
        double result = 2;

        if (daysRented > 2)
            result += (daysRented -2) * 1.5;

        return result;
    }
}

class Movie {

    public static final int CHILDRENS   = 2;
    public static final int REGULAR     = 0;
    public static final int NEW_RELEASE = 1;

    private String _title;
    private Price _price;

    public Movie(String title, int priceCode) {
        _title = title;
        // Stateパターンはここにセッターを使うらしい
        setPriceCode(priceCode);
    }

    public int getPriceCode() {
        return _price.getPriceCode();
    }

    public void setPriceCode(int arg) {
        switch(arg) {
            case REGULAR:
                _price = new RegularPrice();
                break;
            case NEW_RELEASE:
                _price = new NewReleasePrice();
                break;
            case CHILDRENS:
                _price = new ChildrenPrice();
                break;
            default:
                throw new IllegalArgumentException("不正な料金コード");
        }
    }

    double getCharge(int daysRented) {
        return _price.getCharge(daysRented);
    }

    public String getTitle() {
        return _title;
    }

    /* 何やってるのかわかりにくいので、関数の移動に加えて変数名をわかりやすくする
     * Rental each -> aRental
     * result -> Result
     * もとはCustomerクラスのものだったけど、Customerの情報つかってないのでこちらに移動
     */
    int getFrequentRenterPoints(int daysRented) {
        // 新作を二日以上借りた場合はボーナスポイント
        return _price.getFrequentRenterPoints(daysRented);
    }
}

class Rental {
    private Movie _movie;
    private int _daysRented; // 借りた日数

    public Rental(Movie movie, int daysRented) {
        _movie = movie;
        _daysRented = daysRented;
    }

    public int getDaysRented() {
        return _daysRented;
    }

    public Movie getMovie() {
        return _movie;
    }

    double getCharge() {
        return _movie.getCharge(_daysRented);
    }

    int getFrequentRenterPoints() {
        return _movie.getFrequentRenterPoints(_daysRented);
    }

}

class Customer {
    private String _name;
    private Vector<Rental> _rentals = new Vector<Rental>();

    public Customer(String name) {
        _name = name;
    }

    public void addRental (Rental arg) {
        _rentals.addElement(arg);
    }

    public String getName() {
        return _name;
    }
    public String statement() {
        // totalAmountもなくしちゃう
        // double totalAmount       = 0;
        // frequentRenterPointsもなくしちゃう
        // int frequentRenterPoints = 0;
        Enumeration rentals      = _rentals.elements();
        String result            = "Rental Record for " + getName() + "\n";

        while(rentals.hasMoreElements()){
            // 一時変数はなるべく少ないほうがいいんだって
            double thisAmount = 0;
            Rental each = (Rental)rentals.nextElement();

            // 別の関数にしちゃう
            //thisAmount = each.getCharge();

            // レンタルポイントを加算
            //frequentRenterPoints += each.getFrequentRnterPoints();

            // この貸し出しに関する数値の表示
            result += "\t" + each.getMovie().getTitle()
                   + "\t"
                   + String.valueOf(each.getCharge()) + "\n";

            // ここで2回呼び出すのは冗長じゃないのか？
            // やっぱ冗長みたいだったので消す
            //totalAmount += each.getCharge();
        }
        // フッタ部分の追加

        result += "Amount owd is " 
               + String.valueOf(getTotalCharge()) + "\n";

        result += "You earned" 
               + String.valueOf(getTotalFrequentRenterPoints())
               + " frequent renter points";

        return result;
    }

    public String htmlStatement() {

        Enumeration rentals = _rentals.elements();
        String result = "<h1> Rentals for <em>" + getName() + "</em></h1><p>\n";

        while (rentals.hasMoreElements()) {

            Rental each = (Rental) rentals.nextElement();
            result += each.getMovie().getTitle()
                      + ":"
                      + String.valueOf(each.getCharge())
                      + "<br>=n";
        }

        // footer
        result += "<p>You owd <em>" + getName() + "</em></h1><p>=n";
        result += "On this rental you earned <em>"
                  + String.valueOf(getTotalFrequentRenterPoints())
                  + "</em> frequent renter points<p>";

        return result;
    }

    private double getTotalCharge() {
        double result = 0;
        Enumeration rentals = _rentals.elements();

        while (rentals.hasMoreElements()) {

            Rental each = (Rental) rentals.nextElement();
            result += each.getCharge();

        }
        return result;
    }

    private int getTotalFrequentRenterPoints() {

        int result = 0;
        Enumeration rentals = _rentals.elements();

        while(rentals.hasMoreElements()) {

            Rental each = (Rental) rentals.nextElement();
            result += each.getFrequentRenterPoints();

        }
        return result;
    }

}

