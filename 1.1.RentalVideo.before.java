import java.util.*;
/*
 * ビデオのレンタル料金を計算して計算書を印刷する
 * 入力 どの映画 借りる日数
 * 出力 映画の分類を判定(一般、子供向け、新作)
 * 新作かどうかによってレンタルポイントもつく
 */

public class Movie {

    public static final int CHILDRENS   = 2;
    public static final int REGULAR     = 0;
    public static final int NEW_RELEASE = 1;

    private String _title;
    private int _priceCode;

    public Movie(String title, int priceCode) {
        _title     = title;
        _priceCode = priceCode;
    }

    public int getPriceCode() {
        return _priceCode;
    }

    public int setPriceCode(int arg) {
        _priceCode = arg;
    }

    public String getTitle() {
        return _title;
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
        // この時点では金額の計算方法がよくわからない
        double totalAmount       = 0;
        int frequentRenterPoints = 0;
        Enumeration rentals      = _rentals.elements();
        String result            = "Rental Record for " + getName() + "\n";

        while(rentals.hasMoreElements()){
            double thisAmount = 0;
            Rental each = (Rental) rentals.nextElement();

            // 一行ごとに金額を計算
            // 初期値 + 借りた日数 + たくさん借りた日数
            // 業務ロジックが特殊
            switch (each.getMovie().getPriceCode()) {

                case Movie.REGULAR:
                    thisAmount += 2;
                    if (each.getDaysRented() > 2)
                        thisAmount += (each.getDaysRented() -2) * 1.5;
                    break;

                case Movie.NEW_RELEASE:
                    thisAmount += each.getDaysRented() * 3;
                    break;

                case Movie.CHILDRENS:
                    thisAmount += 1.5;
                    if (each.getDaysRented() > 3)
                        thisAmount += (each.getDaysRented() -3) * 1.5;
                    break;
            }
            // レンタルポイントを加算
            frequentRenterPoints ++;
            // 新作を二日以上借りた場合はボーナスポイント
            if((each.getMovie().getPriceCode() == Movie.NEW_RELEASE) &&
                each.getDaysRented() > 1) frequentRenterPoints ++;

            // この貸し出しに関する数値の表示
            result += "\t" + each.getMovie().getTitle()
                   + "\t"
                   + String.valueOf(thisAmount) + "\n";

            totalAmount += thisAmount;
        }
        // フッタ部分の追加
        result += "Amount owd is " 
               + String.valueOf(totalAmount) + "\n";

        result += "You earned" 
               + String.valueOf(frequentRenterPoints)
               + " frequent renter points";

        return result;
    }
}

