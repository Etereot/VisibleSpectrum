package com.etereot.visiblespectrum;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Admin on 15/01/2015.
 */


 public class Recta{

     private PointF p1;
     private PointF p2;

     private PointF v;
     private PointF n;
     private double C;

     private ArrayList recta;



     public Recta(PointF a,PointF b){

         p1 = a;
         p2 = b;

         v = new PointF(b.x-a.x,b.y-a.y);
         n = new PointF(-v.y,v.x);
         C = (-n.x*p1.x) - (n.y*p1.y);

         recta = new ArrayList(3);
         recta.add(n.x);
         recta.add(n.y);
         recta.add(C);


     }

    public Recta(PointF a,PointF v, boolean nada){

        p1 = a;

        this.v = v;
        n = new PointF(-v.y,v.x);
        C = (-n.x*p1.x) - (n.y*p1.y);

        recta = new ArrayList(3);
        recta.add(n.x);
        recta.add(n.y);
        recta.add(C);



    }

     public ArrayList getRecta() {
         return recta;
     }

    public PointF ChoqueRectas(Recta a){

        double determinante = (n.x*a.n.y)-(n.y*a.n.x);
        PointF vuelta = new PointF();

        double x = ((C*a.n.y)-(n.y*a.C)) / determinante;
        double y = ((n.x*a.C)-(C*a.n.x)) / determinante;

        vuelta.set((float)x,(float)y);

        if(a.p1.x<x && x<a.p2.x && a.p1.y<y && y<a.p2.y) return vuelta;
        else return null;

    }

    public double getPunto(boolean a){
        if(a) return p1.x;
        else return p1.y;
    }

    public PointF ChoqueBordes(){


        //pared izquierda, x=0
        double y = -C / n.y;
        if (y>0 && y<MyView.mCanvasHeight) return new PointF(0,(float)y);
        //pared arriva, y=0
        double x = -C / n.x;
        if (x>0 && x<MyView.mCanvasWidth) return  new PointF((float)x,0);
        //pared derecha
        y = (-C - MyView.mCanvasWidth*n.x)/n.y;
        return new PointF((float)MyView.mCanvasWidth,(float)y);


    }




 }



