package com.etereot.visiblespectrum;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Admin on 11/01/2015.
 */
public class Triangle {

    private final Path path;

    private PointF v1;
    private PointF v2;
    private PointF v3;


    private Recta Lado1;
    private Recta Lado2;
    private Recta Lado3;
    private final ArrayList Lados;



    private ArrayList Vertices_dentro;







    public Triangle(){

        path = new Path();
        v1 = new PointF();
        v2 = new PointF();
        v3 = new PointF();


        Lados = new ArrayList(3);

        Vertices_dentro = new ArrayList();





    }

    public void setTriangle(PointF p1,PointF p2,PointF p3){

        v1 = p1;
        v2 = p2;
        v3 = p3;


        Lado1 = new Recta(v1,v2);
        Lado2 = new Recta(v2,v3);
        Lado3 = new Recta(v1,v3);

        Lados.add(Lado1);
        Lados.add(Lado2);
        Lados.add(Lado3);


        path.moveTo(v1.x, v1.y);
        path.lineTo(v2.x, v2.y);
        path.lineTo(v3.x, v3.y);


        path.close();


    }

    public void drawTriangle(Canvas canvas,Paint paint){
        canvas.drawPath(path,paint);
    }


    //Devuelve un vertice, segun el int que pases
    public PointF getCoordenadas(int i){
        if (i==1) return v1;
        else if (i==2) return v2;
        else if (i==3) return v3;
        else return null;

    }




    public ArrayList getLados(){

        return Lados;

    }



    public boolean marcao(){
        return !Vertices_dentro.isEmpty();
    }

    public PointF getCoord(PointF p,boolean i){

        for (int j=0;j<2;j++){

        }



    }





}


