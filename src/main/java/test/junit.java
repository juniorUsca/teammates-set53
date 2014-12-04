package test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class junit {
    public int multiply(int num1,int num2){
        return num1 * num2;
    }
    
    public boolean isNumeric(String cad){
        try{
            Integer.parseInt(cad);
            return true;
        }catch(NumberFormatException nfe){
            return false;
        }
    }
    
    
public String verificar_anio(String anio){
        
        if(anio.equals("")){
            return "Anio no puede ser vacio";
        }
        if(isNumeric(anio)){
            Integer aux=Integer.parseInt(anio);
            if(aux<0){
                return "Anio incorrecto";
            }
            if(aux>=0 && aux<2000){
                return "No hay registros en la base de datos";
            }
            Date ahora = new Date();
            SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
            if(aux>Integer.parseInt(formateador.format(ahora))){
                return "Anio futuro";
            }else{
                return "OK";
            }
        }else{
            return "Anio incorrecto";
        }
        
    }
}
