package time;

public abstract class Yaz {
    private static int yaz=1;

    public static int getYaz() {
        return yaz;
    }

    public static void advanceYaz(int yaz) {
        Yaz.yaz += yaz;
    }
}
