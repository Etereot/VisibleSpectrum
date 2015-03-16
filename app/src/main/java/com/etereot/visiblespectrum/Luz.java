package com.etereot.visiblespectrum;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Admin on 12/01/2015.
 */
public class Luz {

    private PointF inicio;

    private PointF direccion;

    private PointF posicion;

    private PointF normal;



    private Path path;


    private double C;

    private int tLuz=5;

    private Recta izRecta;
    private Recta deRecta;

    private ArrayList PuntosDeChoqueiz;
    private ArrayList PuntosDeChoquede;









    public Luz(){

        normal = new PointF();
        posicion = new PointF();



    }

    public void setLuz(PointF punto){

        PointF uso = new PointF();
        double modulo;

        inicio = punto;
        //vector direccion
        direccion = punto;
        direccion.y = direccion.y - 100;


        //normal del vector direccion
        modulo = Math.sqrt(punto.y*punto.y + punto.x * punto.x);
        normal.set((float)(-punto.y/modulo),(float)(punto.x/modulo));

        //producto escalar,la variable c de la ecuacion de la recta
        C = (-normal.x*inicio.x) - (normal.y*inicio.y);

        //posicion actual del punto pa detectar vertices cercanos
        posicion.set(inicio.x,inicio.y);

        uso.set(inicio.x-tLuz,inicio.y);
        izRecta = new Recta(uso,direccion,true);

        uso.set(inicio.x+tLuz,inicio.y);
        deRecta = new Recta(uso,direccion,true);

        PuntosDeChoquede=new ArrayList(10);
        PuntosDeChoquede.add(deRecta);
        PuntosDeChoqueiz=new ArrayList(10);
        PuntosDeChoqueiz.add(izRecta);


    }

    public void UpdateLuz(ArrayList objetos){

        //No debiera de aber nada aqui, la mitad de lo que ai abajo no es mu eficiente
        boolean fin = true;
        double distance;
        Triangle choque;



        PointF vertice;
        int D_I=0;






        while (fin){


            if(!objetos.isEmpty()){
                for(int i=0;i<objetos.size();i++){

                    choque = (Triangle)objetos.get(i);

                    Calcula(choque,false);

                    /*for (int j = 1; j <4 ; j++) {

                        vertice = choque.getCoordenadas(j);

                        distance = vertice.x*normal.x+vertice.y*normal.y+C / Math.sqrt(normal.x*normal.x + normal.y*normal.y);

                        if (distance>0) D_I++;
                        else D_I--;



                        if(Math.abs(distance)<tLuz) choque.setVertices_dentro(vertice);

                    }

                    if(D_I!=3 && D_I!=-3 || choque.marcao()) Calcula(choque);*/




                }



            }



            //posicion.set(posicion.x + direccion.x,posicion.y + direccion.y);




        }






    }

    private void Calcula(Triangle objeto,boolean lado){

        ArrayList Lados = objeto.getLados();

        PointF uno,dos;



        uno=izRecta.ChoqueRectas((Recta)Lados.get(1));


        for(int i=2;i<4;i++){
            dos=izRecta.ChoqueRectas((Recta)Lados.get(i));

            if(uno==null) {uno=dos; continue;}
            if(dos==null) continue;
            if(uno.x>dos.x || uno.y>dos.y) uno = dos;
        }

        PuntosDeChoqueiz.add(uno);










    }


    public void drawLuz(Canvas canvas,Paint paint){canvas.drawPath(path,paint);}

}
