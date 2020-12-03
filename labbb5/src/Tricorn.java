import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator
{
    public static final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    public int numIterations(double x, double y)
    {
        int iteration = 0;
        double zA = 0;
        double zB = 0;
        double zAB = zA * zA + zB * zB;

        while (iteration < MAX_ITERATIONS && zAB < 4){
            double zA1 = zA * zA - zB *zB + x;
            double zB1 = -2 * zA * zB + y;

            zA = zA1;
            zB = zB1;
            zAB = zA * zA + zB * zB;
            iteration += 1;
        }

        if (iteration == MAX_ITERATIONS) return -1;
        return iteration;
    }
    

    public String toString() {
        return "Tricorn";
    }

}