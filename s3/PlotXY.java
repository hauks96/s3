package s3;

import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Point2D;
import java.awt.*;

public class PlotXY {
    private String graphName;
    public double max_x;
    public double min_x;
    public double max_y;
    public double min_y;
    private double [] x_vals;
    private double [] y_vals;
    private double x_interval;
    private double y_interval;
    private String x_name;
    private String y_name;
    private Draw graph;
    private boolean x_set;
    private boolean y_set;
    private boolean data_set;
    private String graphTitle="";
    public double MAX_GRIDS = 15;
    private Boolean enumerate_points = false;


    public PlotXY(String x_name, String y_name, String graph_name){
        this.graphName = graph_name;
        this.graph = new Draw(this.graphName);
        this.graph.enableDoubleBuffering();
        this.x_set = false;
        this.y_set = false;
        this.data_set = false;
        this.x_name = x_name;
        this.y_name = y_name;
        x_vals = new double[0];
        y_vals = new double[0];
    }

    public void graphTitle(String graph_title){
        this.graphTitle = graph_title;
    }

    private void set_title(){
        this.graph.setPenColor(Color.RED);
        if (this.min_x<0){
            this.graph.text((this.max_x+this.min_x)/2, this.max_y+this.max_y*0.15, this.graphTitle);
        }
        else {
            this.graph.text(this.max_x/2, this.max_y+this.max_y*0.15, this.graphTitle);
        }

    }

    // Initialize graph scale and x,y axis
    private void init_graph(){
        this.graph.setPenColor(Color.black);
        this.graph.setPenRadius();

        //X SCALE
        if (this.min_x>=0){
            this.graph.setXscale(0-(this.max_x*0.2), this.max_x+this.max_x*0.2);
        }
        else {
            this.graph.setXscale(this.min_x-(this.max_x*0.2), this.max_x+this.max_x*0.2);
        }

        //Y SCALE
        if (this.min_y>=0){
            this.graph.setYscale(0-this.max_y*0.2, this.max_y+this.max_y*0.2);
        }
        else {
            this.graph.setYscale(this.min_y-(this.max_y*0.2), this.max_y+this.max_y*0.2);
        }

        this.graph.setPenRadius();
    }

    // Set new data to graph
    public void setCoords(double [] x_values, double [] y_values) throws Exception {
        if (x_values.length!=y_values.length) throw new Exception("X and Y values must have same amount of items");
        this.data_set=true;
        if(this.y_vals.length!=0 || this.x_vals.length!=0){
            reset();
        }
        this.x_vals = x_values;
        this.y_vals = y_values;
        get_max_min();
    }

    public void setPoints(Point [] points){
        this.data_set=true;
        double[] x_coords = new double [points.length];
        double[] y_coords = new double [points.length];
        for (int i=0; i<points.length; i++){
            x_coords[i]=points[i].x;
            y_coords[i]=points[i].y;
        }
        this.x_vals = x_coords;
        this.y_vals = y_coords;
        get_max_min();
    }

    public void setPoints2D(Point2D [] points){
        this.data_set=true;
        double[] x_coords = new double [points.length];
        double[] y_coords = new double [points.length];
        for (int i=0; i<points.length; i++){
            x_coords[i]=points[i].x();
            y_coords[i]=points[i].y();
        }
        this.x_vals = x_coords;
        this.y_vals = y_coords;
        get_max_min();
    }

    private void plot_data(){
        this.graph.setPenRadius(0.01);
        this.graph.setPenColor(Draw.BLUE);
        for (int i=0; i<this.x_vals.length; i++){
            this.graph.point(this.x_vals[i], this.y_vals[i]);
            if (this.enumerate_points){
                this.graph.text(this.x_vals[i], this.y_vals[i]+this.max_y*0.05, "P"+(i+1));
            }
        }
    }

    public void enumPoints(){
        if (this.enumerate_points) this.enumerate_points=false;
        else this.enumerate_points = true;
    }

    private double log2(double val){
        if (val<=0) throw new IllegalArgumentException("Cannot plot undefined (Less than zero)");
        return Math.log(val);
    }

    private void plot_data_log(){
        this.graph.setPenRadius(0.01);
        this.graph.setPenColor(Draw.BLUE);
        for (int i=0; i<this.x_vals.length; i++){
            double x = log2(this.x_vals[i]);
            double y = log2(this.y_vals[i]);
            this.graph.point(x, y);
        }
    }

    public void draw(){
        check_max_min();
        if (!this.x_set || !this.y_set) auto_interval();
        init_graph();
        set_title();
        set_y();
        set_x();
        xName(this.x_name);
        yName(this.y_name);
        plot_data();
        this.graph.show();
        this.x_set=false;
        this.y_set=false;
    }

    public void drawLog() {
        check_max_min();
        String hold_x_name = this.x_name;
        String hold_y_name = this.y_name;
        double hold_max_x = this.max_x;
        double hold_max_y = this.max_y;

        this.max_x = log2(this.max_x);
        this.max_y = log2(this.max_y);

        this.x_name = "log("+this.x_name+")";
        this.y_name = "log("+this.y_name+")";

        if (!this.x_set || !this.y_set) auto_interval();
        reset();
        init_graph();
        set_title();
        set_y();
        set_x();
        xName(this.x_name);
        yName(this.y_name);
        plot_data_log();
        this.max_x = hold_max_x;
        this.max_y = hold_max_y;
        this.y_name = hold_y_name;
        this.x_name = hold_x_name;
        this.graph.show();
        this.x_set=false;
        this.y_set=false;
    }

    private void check_max_min(){
        if ((this.max_x==0&&this.min_x==0)||(this.max_y==0&&this.min_y==0)){
            throw new IllegalStateException("Must manually set max/min for x and y axis if no data has been set." +
                    " Default is 0 for both. One of which must be changed.");
        }
        if(this.x_vals.length==0 || this.y_vals.length==0){
            throw new IllegalStateException("Must set values to plot before drawing graphs");
        }
    }

    private void get_max_min(){
        if(this.x_vals.length==0){
            this.max_y=0;
            this.max_x=0;
            return;
        }
        double curr_val_x;
        double curr_val_y;

        double x_max = this.x_vals[0];
        double x_min = 0;
        double y_max = this.y_vals[0];
        double y_min = 0;
        for (int i=0; i<this.x_vals.length; i++){
            curr_val_x = this.x_vals[i];
            curr_val_y = this.y_vals[i];
            if (curr_val_y>y_max) y_max=curr_val_y;
            if (curr_val_y<y_min) y_min=curr_val_y;
            if (curr_val_x>x_max) x_max=curr_val_x;
            if (curr_val_x<x_min) x_min=curr_val_x;
        }
        this.max_x = x_max;
        this.min_x = x_min;
        this.max_y = y_max;
        this.min_y = y_min;
    }

    public void auto_interval(){
        if (!this.x_set){
            this.x_interval = optimal_interval_x();
        }
        if (!this.y_set){
            this.y_interval = optimal_interval_y();
        }
    }

    private double optimal_interval_x(){
        double max=this.max_x;

        if (this.min_x<0&&((-this.min_x)>this.max_x)){
            max = -this.min_x;
        }
        double ret_val = checkIntervals(max);

        if (ret_val!=0){
            if(((-this.min_x/ret_val)+(this.max_x/ret_val))>=this.MAX_GRIDS)
                ret_val+=ret_val;

            return ret_val;
        }

        else {
            ret_val = (double) Math.round(max/10);
            if (ret_val>10)ret_val = ret_val-ret_val%10;
            this.max_x = Math.ceil(this.max_x/ret_val)*ret_val;

        }
        if(this.min_x!=0){
            if(this.min_x<=0) {
                this.min_x = Math.floor(this.min_x/ret_val)*ret_val;
            }
            else {
                this.min_x = Math.ceil(this.min_x/ret_val)*ret_val;
            }
        }

        if(((-this.min_x/ret_val)+(this.max_x/ret_val))>=this.MAX_GRIDS){
            ret_val+=ret_val;
        }
        return ret_val;
    }

    private double optimal_interval_y(){
        double max=this.max_y;

        if ((-this.min_y)>this.max_y){
            max = -this.min_y;
        }
        double ret_val = checkIntervals(max);

        if (ret_val!=0){
            if(((-this.min_y/ret_val)+(this.max_y/ret_val))>=this.MAX_GRIDS)
                ret_val+=ret_val;

            return ret_val;
        }

        else {
            ret_val = (double) Math.round(max/10);
            if (ret_val>10) ret_val = ret_val-ret_val%10;
            this.max_y = Math.ceil(this.max_y/ret_val)*ret_val;
        }
        if (this.min_y!=0){
            if(this.min_y<0) {
                this.min_y = Math.floor(this.min_y/ret_val)*ret_val;
            }
            else {
                this.min_y = Math.ceil(this.min_y/ret_val)*ret_val;
            }
        }

        if(((-this.min_y/ret_val)+(this.max_y/ret_val))>=this.MAX_GRIDS){
            ret_val+=ret_val;
        }

        return ret_val;
    }

    private double checkIntervals(double max){
        double ret_val=0;
        if (max<0) {
            max = -max;
        }
        if (max<=10 && max>5){
            ret_val = 1;
        }
        if (max<=5 && max>2){
            ret_val = 0.5;
        }
        if (max<=2 && max>1){
            ret_val = 0.25;
        }
        if (max<=1 && max>0){
            ret_val = 0.1;
        }
        return ret_val;
    }

    public void set_x_interval(double x_interval){
        this.x_interval = x_interval;
        this.x_set = true;
    }

    public void set_y_interval(double y_interval){
        this.y_interval = y_interval;
        this.y_set = true;
    }

    private void set_y(){
        double y=0;
        if (this.min_y<0){
            y=this.min_y;
        }
        this.graph.setPenColor(Color.BLACK);

        //Y AXIS
        if(this.min_y>0){
            this.graph.line(0, 0, 0, this.max_y); //Y axis
        }
        else{
            this.graph.line(0, this.min_y, 0, this.max_y); //Y axis
        }

        int ten_power = getPowerTen(this.max_y);
        while(y<=this.max_y){
            this.graph.setPenColor(Color.BLACK);
            if (ten_power>=2){
                String y_number = getScientificString(y, ten_power);
                this.graph.text(-max_x*0.07, y, y_number);
            }
            else {
                if (y==(double)((int)y))
                    this.graph.text(-max_x*0.07, y, (int)y+"");
                else{
                    this.graph.text(-max_x*0.07, y, ((float)y)+"");
                }
            }

            this.graph.setPenColor(Color.LIGHT_GRAY);
            if(min_x<0){
                this.graph.line(min_x, y, max_x, y);
            }
            else{
                this.graph.line(0, y, max_x, y);
            }

            y += this.y_interval;
        }
        if (ten_power>=2){
            this.y_name += " (10^"+ten_power+")";
        }
    }

    private void set_x() {
        double x=0;
        if (this.min_x<0) x=this.min_x;
        this.graph.setPenColor(Color.BLACK);
        //X AXIS
        if (this.min_x>0){
            this.graph.line(0, 0, this.max_x, 0);//X axis
        }
        else {
            this.graph.line(this.min_x, 0, this.max_x, 0);//X axis
        }
        int ten_power = getPowerTen(this.max_x);
        while(x<=this.max_x){
            this.graph.setPenColor(Color.BLACK);
            if (ten_power>=2){
                String x_number = getScientificString(x, ten_power);
                this.graph.text(x, -this.max_y*0.07, x_number);
            }
            else {
                if(x==(double)((int)x)) this.graph.text(x, -this.max_y*0.07, (int)x+"");
                else this.graph.text(x, -this.max_y*0.07, ((float)x)+"");
            }
            this.graph.setPenColor(Color.LIGHT_GRAY);
            if (this.min_y<0) this.graph.line(x, this.min_y, x, this.max_y);
            else this.graph.line(x, 0, x, this.max_y);
            x += this.x_interval;
        }
        if (ten_power>=2){
            this.x_name +=" (10^"+ten_power+")";
        }
    }

    private String getScientificString(double number, int ten_to_power){
        // a*ex -> a*10^x
        boolean is_neg = false;
        if (number<0) is_neg = true;
        double a = number/Math.pow(10, ten_to_power);
        a = (double) Math.round(a * 10) / 10;
        return ""+a;
    }

    private int getPowerTen(double max_val){
        return (int) Math.floor(Math.log10(max_val));
    }

    private void xName(String x_axis){
        this.graph.setPenColor(Color.BLACK);
        this.x_name = x_axis;
        this.graph.text(this.max_x+this.max_x*0.09, 0, x_axis);
    }

    private void yName(String y_axis){
        this.graph.setPenColor(Color.BLACK);
        this.y_name = y_axis;
        this.graph.text(0, this.max_y+this.max_y*0.09, y_axis);
    }

    public void set_max_x(double max_x){
        this.max_x = max_x;
    }

    public void set_min_x(double min_x){
        this.min_x = min_x;
    }

    public void set_max_y(double max_y){
        this.max_y = max_y;
    }

    public void set_min_y(double min_y){
        this.min_y = min_y;
    }

    private void reset(){
        this.graph = new Draw(this.graphName);
    }

    public void drawGreenPoint(double x, double y){
        this.graph.setPenColor(Color.green);
        drawPoint(x, y);
    }

    public void drawRedPoint(double x, double y){
        this.graph.setPenColor(Color.red);
        drawPoint(x, y);
    }

    public void drawYellowPoint(double x, double y){
        this.graph.setPenColor(Color.yellow);
        drawPoint(x, y);
    }

    public void drawBluePoint(double x, double y){
        this.graph.setPenColor(Color.blue);
        drawPoint(x, y);
    }

    public void drawPoint(double x, double y){
        this.graph.setPenRadius(0.01);
        this.graph.point(x, y);
        this.graph.show();
    }

    public static void main(String[] args) throws Exception {
        double [] x_data = new double[10000];
        double [] y_data = new double[10000];
        PlotXY graph = new PlotXY("x", "y", "XY Graph");
        graph.graphTitle("NORMAL GRAPH EXAMPLE");
        graph.set_max_x(20000);
        graph.set_max_y(20000);

        for(int i =0; i<10000; i++){
            x_data[i]=i;
            y_data[i]=i;
        }
        graph.setCoords(x_data, y_data);
        graph.set_max_x(20000);
        graph.set_max_y(20000);
        graph.draw();
        //graph.graph_title("LOG GRAPH EXAMPLE");
        //graph.draw_log();
    }

}
