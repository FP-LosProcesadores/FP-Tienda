/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp.proyectofinal;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Enano
 */
public class FPProyectoFinal {
    public static int  nOfPurchases=0;
    public static int dayEarnings=0;
    public static String dayItemsPurchased="";
    public static String inputCodes="";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Menu();
    }
    public static void Menu ()
    {
        String option; 
        System.out.println("Bienvenido a Mi tiendita");
        System.out.println("Por favor introduzca una opción del menú: ");
        System.out.println("------------------------------------------");
        System.out.println("1. Inventario");
        System.out.println("2. Venta");
        System.out.println("3. Cierre");
        System.out.println("4. Salir");
        Scanner in = new Scanner(System.in);
        option = in.nextLine();
        switch (option) //Evalua con un switch las opciones posibles.
        {
            case "1":
                invMenu();
                break;
            case "2":
                nOfPurchases++;
                saleMenu(nOfPurchases);
                break;
            case "3":
                close();
                break;
            case "4":
                System.out.println("Gracias por usar el programa!");
                System.exit(0);
                break;
            default:    //En caso de no elegir ninguna de las opciones disponibles, termina el programa.
                System.out.println("Opción no válida, vuelva a ingresar una opción");
                Menu();
                break;
        }
    }
    public static void invMenu()
    {
        System.out.println("------------------------------------------");
        System.out.println("1. Desplegar inventario");
        System.out.println("2. Agregar un nuevo producto");
        System.out.println("3. Regresar");
        int opc=getInt(" una opción");
        switch (opc)
        {
            case 1:
                showFile("inventory.txt");
                invMenu();
                break;
            case 2:
                newItems();
                break;
            case 3:
                Menu();
                break;
            default:
                System.out.println("ERROR");
                invMenu();
                break;
        }
    }
    public static void newItems()
    {
        Scanner ing = new Scanner (System.in);
        try
        {
            System.out.println("¿Cuantos productos desea agregar?");
            int times = ing.nextInt(); String code;
            String[][] prevItemInfo=loadInventory();
            String[][] itemInfo = new String[1][4];
            for(int i=0;i<times;i++)
            {
                System.out.println("Registro #"+(i+1));
                code=getString("clave del producto");
                for (int j=0; j<itemInfo.length; j++) 
                {
                    while (prevItemInfo[j][0].contains(code)||inputCodes.equals(code))
                    {
                        System.err.println("Esta clave ya existe.");
                        code=getString("otra clave del producto");
                    }
                }
                itemInfo[0][0]=code;inputCodes+=code;
                itemInfo[0][1]=""+getInt("cantidad del producto");
                itemInfo[0][2]=getString("descripción del producto");
                itemInfo[0][3]=""+getInt("precio del producto");      
            }
            System.out.println("Se han agregado los productos, se le redireccionará al menú");
            createFile(itemInfo,"inventory.txt",false);
            Menu();
        }
        catch (NumberFormatException e)
        {
            System.err.println("Por favor, ingrese un número valido");
        }
    }
    public static void createFile(String[][] arrayInput, String fileName, boolean edit) //Se encarga de crear el archivo, utilizando la información del arreglo
    {                                                                 //y el nombre de archivo deseado
        File file = new File (fileName); //Se crea un archivo utilizando el nombre
        if(edit==true)
        {
            file.delete();
        }
        if (file.exists()==false) //Se comprueba que se haya creado
        {   
            try //Lo intenta, en caso de errores.
            {  
                file.createNewFile();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();   //Aparte del de sistema.
                createFile(arrayInput, fileName, false);   //Se reinicia el método.
            }
        }
        try //Lo intenta, en caso de errores
        {
            PrintWriter write = new PrintWriter (new FileOutputStream(file, true));//new FileOutputStream(file, true)Instancia un objeto tipo PrintWriter, el cual escribe caracter por caracter.
            for(int j=0;j<arrayInput.length;j++)
            {
                for(int i=0;i<arrayInput[j].length;i++)    //Recorre el arreglo.
                {
                    write.append(arrayInput[j][i]+"|");  //Arrays.toString toma como parámetro el arreglo, y lo escribe en el documento.
                }
                if(edit!=true)
                {
                    if(j>=arrayInput.length)
                    {
                        write.println();
                    }
                }
            }
            write.close();  //Se cierra el archivo.
        }
        catch (FileNotFoundException e) //En caso de error, imprime el log del error.
        {
            e.printStackTrace();
        } 
    }
    public static void createFile(String inputAppend, String fileName, boolean edit) //Se encarga de crear el archivo, utilizando la información del arreglo
    {                                                                 //y el nombre de archivo deseado
        File file = new File (fileName); //Se crea un archivo utilizando el nombre
        if(edit==true)
        {
            file.delete();
        }
        if (file.exists()==false) //Se comprueba que se haya creado
        {   
            try //Lo intenta, en caso de errores.
            {  
                file.createNewFile();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();   //Aparte del de sistema.
                createFile(inputAppend, fileName, false);   //Se reinicia el método.
            }
        }
        try //Lo intenta, en caso de errores
        {
            PrintWriter write = new PrintWriter (new FileOutputStream(file, true));//new FileOutputStream(file, true)Instancia un objeto tipo PrintWriter, el cual escribe caracter por caracter.
            write.append(inputAppend);  //Arrays.toString toma como parámetro el arreglo, y lo escribe en el documento.
            write.println("");
            write.println("-------------------------");
            write.close();  //Se cierra el archivo.
        }
        catch (FileNotFoundException e) //En caso de error, imprime el log del error.
        {
            e.printStackTrace();
        } 
    }
    public static int getInt(String data)
    {
        Scanner ing = new Scanner (System.in);
        boolean error; int number=0;
        System.out.println("Ingrese "+data+" :");
        do
        {
            if(ing.hasNextInt())
            {
                number=ing.nextInt();
                error=false;
            }
            else
            {
                System.out.println("Valor inválido, por favor vuelva a intentarlo"); 
                ing.nextLine();
                error=true;
            }
        }
        while(error==true);
        return number;
    }

    public static String getString(String data)
    {
        Scanner ing = new Scanner (System.in);
        System.out.println("Ingrese: "+data);
        return ing.nextLine();
    }
    public static void saleMenu(int nOfPurch)
    {
        String[][] itemInfo = loadInventory();int amount=0, sale=0; boolean error = true;
        Scanner ing = new Scanner(System.in); 
        ing.useDelimiter("\\*");
        System.out.println("Ingrese la compra de los artículos, utilizando el formato: #*clave, e ingrese 'q' para terminar la ejecución. ");
        String itemBuy = ing.nextLine();
        while(error==true)
        {
            try
            {
                while(itemBuy.equals("q")==false||!itemBuy.equals("Q")==false)
                {        
                    String[] parts = itemBuy.split("\\*");
                    amount=Integer.parseInt(parts[0]);
                    String code=parts[1];
                    for(int j=0;j<itemInfo.length;j++)
                    {
                        if(itemInfo[j][0].contains(""+code))
                        {
                            int inStock=Integer.parseInt(itemInfo[j][1]);
                            if(amount>inStock)
                            {
                                System.out.println("No hay suficientes en el inventario, por lo que solo se compraron: "+inStock);
                                sale+=inStock*Integer.parseInt(itemInfo[j][3]);
                                amount=inStock;
                                AddToTicket(itemInfo[j],amount,nOfPurch);
                                itemInfo[j][1]=String.valueOf(Integer.parseInt(itemInfo[j][1])-amount);
                                DateFormat df = new SimpleDateFormat("MM.dd.yyyy");
                                Date today = Calendar.getInstance().getTime();
                                String reportDate = df.format(today);
                                String fileName="Cierre"+reportDate+".txt";
                                createFile(dayItemsPurchased, fileName,false);
                            }
                            else
                            {
                                inStock-=amount;
                                sale+=amount*Integer.parseInt(itemInfo[j][3]);
                                AddToTicket(itemInfo[j],amount,nOfPurch);
                                itemInfo[j][1]=String.valueOf(Integer.parseInt(itemInfo[j][1])-amount);
                                DateFormat df = new SimpleDateFormat("MM.dd.yyyy");
                                Date today = Calendar.getInstance().getTime();
                                String reportDate = df.format(today);
                                String fileName="Cierre"+reportDate+".txt";
                                createFile(dayItemsPurchased, fileName,false);
                            }
                        }
                    }
                    System.out.println("Venta capturada, puede seguir o presionar 'q' para salir. ");
                    error=false;
                    itemBuy = ing.nextLine();
                }
                dayEarnings+=sale;
                System.out.println("La venta fue exitosa, el total es: "+sale);
                createFile(itemInfo, "inventory.txt",true);
                Menu();
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                System.out.println("No se pudo encontrar el producto, vuelva a ingresarlo: ");
                itemBuy=ing.nextLine();
            }
        }
    }
    public static void AddToTicket(String[] itemInfo, int amount,int nOfPurch)
    {
        String[][] ticket=new String [1][3];
        ticket[0][0]=""+amount;
        ticket[0][1]=itemInfo[2];
        ticket[0][2]=String.valueOf(amount*Integer.parseInt(itemInfo[3]));
        dayItemsPurchased=ticket[0][0]+" "+ticket[0][1]+" "+" "+ticket[0][2];
        DateFormat df = new SimpleDateFormat("MM.dd.yyyy");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        String fileName=reportDate+"("+nOfPurch+")"+".txt";
        createFile(ticket,fileName, false);
    }

    public static String[][] loadInventory() 
    {
        String[][] inventoryArray = null;
        ArrayList<ArrayList<String>> Yinventory = new ArrayList<ArrayList<String>>();
        try
        {
            File file = new File("inventory.txt");
            try (Scanner scan = new Scanner(file))
            {
                scan.useDelimiter("\\|");
                while (scan.hasNext()) 
                {
                    for(int counter=0;counter<3;counter++)
                    {
                        try
                        {
                            ArrayList<String> Xinventory = new ArrayList<String>();
                            Xinventory.add(scan.next());
                            Xinventory.add(scan.next());
                            Xinventory.add(scan.next());
                            Xinventory.add(scan.next());
                            Yinventory.add(counter, Xinventory);
                        }
                        catch (NoSuchElementException e)
                        {
                            counter=4;
                        }
                    }
                }
                // closing the scanner stream
                scan.close();
            }
            inventoryArray = new String[Yinventory.size()][4];
            for (int i=0; i < Yinventory.size(); i++) 
            {
                ArrayList<String> row = Yinventory.get(i);
                inventoryArray[i] = row.toArray(new String[row.size()]);
            }
            return inventoryArray;
        } 
        catch (FileNotFoundException e)
        {
            System.out.println("No se encontraron productos en el inventario, ingrese productos y vuelva a intentarlo.");
        }
        finally
        {
            return inventoryArray;
        }
    }
    public static void close ()
    {
        System.out.println("Se realizará el cierre del día");
        System.out.println("********************************");
        System.out.println("Las compras del día de hoy fueron:");
        DateFormat df = new SimpleDateFormat("MM.dd.yyyy");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        String fileName="Cierre"+reportDate+".txt";
        showFile(fileName);
        System.out.println("********************************");
        System.out.println("Las ganancias del día de hoy son: "+dayEarnings);
        System.out.println("¡Gracias por usar el programa!");
        System.exit(0);
    }
    public static void showFile(String fileName) //Se encarga de leer e imprimir el texto de los archivos.
    {
        Scanner in=new Scanner (System.in);
        File file = new File (fileName); //Pide el nombre del archivo, y le agrega la extensión.
        String cadena="";
        try (FileReader lectura = new FileReader(file)) //Intenta instanciar un objeto FileReader
        {
            BufferedReader bufferL = new BufferedReader(lectura);
            while (cadena!=null) //Mientras la cadena no sea nula
            {
                cadena = bufferL.readLine(); //Lee línea por línea 
                if(cadena!=null)//Si no se encuentra null dentro del archivo
                {
                    System.out.println(cadena); //Imprime la línea.
                }
            }
            bufferL.close();//Se cierra la instancia del BufferedReader y FileReader
            lectura.close();
        } 
        catch (Exception e) //En caso de no poder, manda un mensaje de error
        {
            System.out.println("No se pudo encontrar el archivo, se le regresará al menú principal");
            System.out.println("------------------------------------------------------------------");
            Menu(); //Y regresa al usuario al menú principal.
        } 
    }
}
