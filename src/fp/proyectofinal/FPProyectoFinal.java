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
    public static int  nOfPurchases=0;          //Variable global que se encarga de contar las compras que se han realizado
    public static int dayEarnings=0;            //Variable global que se encarga de contar las ganancias del día
    public static String dayItemsPurchased="";  //Variable global que se encarga de enlistar los productos comprados
    public static List<String> inputCodes= new ArrayList<>();         //Variable global que se encarga de leer los códigos que se han ingresado en esta sesión.
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Menu();         //Se inicializa el menú, que es donde parte el programa.
    }
    public static void Menu ()  //Desplega el menú principal
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
                nOfPurchases++; //Se le agrega al número de ventas, ya que se realizará una.
                saleMenu(nOfPurchases); //Se va al menú de ventas, con el número de la venta actual.
                break;
            case "3":
                close();
                break;
            case "4":
                System.out.println("Gracias por usar el programa!");
                System.exit(0);
                break;
            default:    //En caso de no elegir ninguna de las opciones disponibles, marca el error y reinicia el menú.
                System.out.println("Opción no válida, vuelva a ingresar una opción");
                Menu();
                break;
        }
    }
    public static void invMenu()    //Despliega el menú del inventario.
    {
        System.out.println("------------------------------------------");
        System.out.println("1. Desplegar inventario");
        System.out.println("2. Agregar un nuevo producto");
        System.out.println("3. Regresar");
        int opc=getInt(" una opción");  //Utiliza getInt, para pedir la opción.
        switch (opc)
        {
            case 1:
                showFile("inventory.txt");  //Abre y despliega el inventario, que siempre se encontrará y actualizará en inventory.txt
                invMenu();                  //Regresa al menú despues de desplegarlo.
                break;
            case 2:
                newItems();
                break;
            case 3:
                Menu();
                break;
            default:
                System.out.println("ERROR"); //Marca error, y reinicia el menú
                invMenu();
                break;
        }
    }
    public static void newItems()   //Se encarga de agregar nuevos productos a inventory.txt
    {
        Scanner ing = new Scanner (System.in);  //Se instancia el Scanner
        try
        {
            System.out.println("¿Cuantos productos desea agregar?");
            int times = ing.nextInt(); String code;  //Variables, code se usa para comprobar la clave.
            String[][] prevItemInfo=loadInventory(); //Se cargan los productos que ya estan en existencia
            String[][] itemInfo = new String[1][4];  //Se hace un arreglo bidimensional para guardar el/los productos nuevos
            for(int i=0;i<times;i++)                 //Se repite por el número de artículos a agregar
            {
                System.out.println("Registro #"+(i+1));
                code=getString("clave del producto");//Se utiliza getString para pedir la clave del producto
                for(int j=0; j<prevItemInfo.length; j++) //Se recorre el arreglo de los productos ya existentes
                {
                    while (prevItemInfo[j][0].contains(code)||inputCodes.contains(code)) //Para comprobar si existen,
                    {                                                                    //junto con los nuevos en caso de haber.
                        System.err.println("Esta clave ya existe.");//Manda un error si existe
                        code=getString("otra clave del producto");  //Usa getString para pedir otra clave
                        j=0;                                        //Reinicia j, para que se vuelva a comprobar en todo el arreglo
                    }
                }
                itemInfo[0][0]=code;   //Si el código no esta repetido, se agrega al arreglo
                inputCodes.add(code);  //Se agrega al registro de los códigos agregados en esta sesión
                itemInfo[0][1]=""+getInt("cantidad del producto");  //Utiliza getInt para pedir la cantidad en existencia
                itemInfo[0][2]=getString("descripción del producto"); //Utiliza getString para pedir la descripción
                itemInfo[0][3]=""+getInt("precio del producto");  //Utiliza getInt para pedir el precio
                createFile(itemInfo,"inventory.txt",false); //Agrega los nuevos productos a inventory.txt, o lo crea si no existe.
            }
            System.out.println("Se han agregado los productos, se le redireccionará al menú");
            Menu(); //Regresa al menú
        }
        catch (NumberFormatException e) //catch en caso de que no se ingrese un número valido.
        {
            System.err.println("Por favor, ingrese un número valido");
        }
    }
    public static void createFile(String[][] arrayInput, String fileName, boolean createNew) //Se encarga de crear el archivo, utilizando la información del arreglo
    {                                                                                   //y el nombre de archivo deseado
        File file = new File (fileName); //Se crea un archivo utilizando el nombre
        if(createNew==true)  //el booleano createNew es para comprobar si se quiere volver a hacer el archivo, y no solo agregar/editar.
        {
            file.delete();  //Se borra el archivo, para re-crearlo.
        }
        if (file.exists()==false) //Comprueba si ya existe el archivo
        {   
            try //Lo intenta, en caso de errores.
            {  
                file.createNewFile();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();   //Error del sistema.
                createFile(arrayInput, fileName, false);   //Se reinicia el método.
            }
        }
        try //Lo intenta, en caso de errores
        {
            PrintWriter write = new PrintWriter (new FileOutputStream(file, true));//Instancia un objeto tipo PrintWriter
            for(int j=0;j<arrayInput.length;j++)                                   //utiliza FileOutputStream para poder utilizar .append()
            {
                for(int i=0;i<arrayInput[j].length;i++)    //Recorre el arreglo.
                {
                    write.append(arrayInput[j][i]+"|");  //Agrega nuevas líneas al .txt
                }
                if(createNew!=true)                      //Por razones estéticas, si no es uno nuevo hace nuevas líneas
                {
                    if(j>=arrayInput.length)             //mientras que la línea no sea la última a agregar
                    {                                    //Esto para que no se confunda el programa al leer la última linea
                        write.println();                 //La cual tendría una línea de más   
                    }
                }
            }
            write.close();  //Se cierra la instancia de PrintWriter
        }
        catch (FileNotFoundException e) //En caso de error, imprime el log del error.
        {
            e.printStackTrace();
        } 
    }
    public static void createFile(String inputAppend, String fileName, boolean edit) 
    {                                    //A diferencia del otro createFile, este recibe un String, en lugar de String[][] y funciona igual                           
        File file = new File (fileName); //Se crea un archivo utilizando el nombre
        if(edit==true)
        {
            file.delete();
        }
        if (file.exists()==false) 
        {   
            try 
            {  
                file.createNewFile();
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();  
                createFile(inputAppend, fileName, false);  
            }
        }
        try //Lo intenta, en caso de errores
        {
            PrintWriter write = new PrintWriter (new FileOutputStream(file, true));
            write.append(inputAppend);//Esta es la única diferencia, agrega la línea en lugar de todo el arreglo.
            write.println("");
            write.println("-------------------------");
            write.close(); 
        }
        catch (FileNotFoundException e) //En caso de error, imprime el log del error.
        {
            e.printStackTrace();
        } 
    }
    public static int getInt(String data) //Se encarga de pedir un int, y comprobar que sea un valor válido
    {
        Scanner ing = new Scanner (System.in);
        boolean error; int number=0; 
        //Variables
        System.out.println("Ingrese "+data+" :");
        do
        {
            if(ing.hasNextInt())    //Se puede utilizar un try-catch con NumberFileException, pero es mas elegante
            {                       //utilizar .hasNextInt() para comprobar si es un int.
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
        while(error==true); //Se ejecuta hasta que se ingrese un número, comprobando el booleano error
        return number;
    }

    public static String getString(String data) //Se encarga de pedir un String
    {
        Scanner ing = new Scanner (System.in);
        System.out.println("Ingrese: "+data);
        return ing.nextLine();
    }
    public static void saleMenu(int nOfPurch) //Se encarga de las ventas, utiliza nOfPurch para el # del "ticket" que se crea. 1,2,3....
    {
        String[][] itemInfo = loadInventory(); //Carga inventory.txt
        int amount=0, sale=0; boolean error = true; 
        //Variables
        Scanner ing = new Scanner(System.in); //Se instancia el Scanner
        ing.useDelimiter("\\*");  //Utiliza la clase useDelimiter para separar con "*"
        System.out.println("Ingrese la compra de los artículos, utilizando el formato: #*clave, e ingrese 'q' para terminar la ejecución. ");
        String itemBuy = ing.nextLine();
        while(error==true) //Se repetira mientras que haya un error
        {
            try 
            {
                while(itemBuy.equals("q")==false||!itemBuy.equals("Q")==false) //Checa si el ingreso no es q, lo que significaria salir
                {        
                    String[] parts = itemBuy.split("\\*");  //Utiliza el método .split() de String para separar en 2 el texto que se reciba
                    amount=Integer.parseInt(parts[0]);      //Parsea el primer número, que es la cantidad.
                    String code=parts[1];                   //Se consigue la clave del sistema
                    for(int j=0;j<itemInfo.length;j++)      //Se recorre el arreglo 
                    {
                        if(itemInfo[j][0].contains(code))//Busca la clave en el arreglo, si la contiene...     
                        {
                            int inStock=Integer.parseInt(itemInfo[j][1]); //Guarda en inStock la cantidad de ese producto
                            if(amount>inStock)  //Si se piden más de los que hay en existencia
                            {                   //Manda dicho mensaje, y se compran los máximos posibles
                                System.out.println("No hay suficientes en el inventario, por lo que solo se compraron: "+inStock);
                                sale+=inStock*Integer.parseInt(itemInfo[j][3]); // Le suma a "sale" la venta que se acaba de realizar
                                amount=inStock;                                 //Se iguala amount a inStock, ya que se vendio ese número
                                AddToTicket(itemInfo[j],amount,nOfPurch);       //Se manda a AddToTicket, para que se cree el ticket.
                                itemInfo[j][1]=String.valueOf(Integer.parseInt(itemInfo[j][1])-amount); //Se resta del arreglo los que se compraron
                                DateFormat df = new SimpleDateFormat("MM.dd.yyyy"); //Se crea un formato de fecha
                                Date today = Calendar.getInstance().getTime();      //Se pide la fecha y hora de hoy
                                String reportDate = df.format(today);               //Se formatea la fecha con el formato
                                String fileName="Cierre"+reportDate+".txt";         //Se formatea para el cierre
                                createFile(dayItemsPurchased, fileName,false);      //Se agregará al archivo que será el cierre
                            }
                            else //Si no se piden más de los que hay en existencia
                            { 
                                sale+=amount*Integer.parseInt(itemInfo[j][3]); // Le suma a "sale" la venta que se acaba de realizar
                                AddToTicket(itemInfo[j],amount,nOfPurch);      //Se ejecuta de la misma manera 
                                itemInfo[j][1]=String.valueOf(Integer.parseInt(itemInfo[j][1])-amount);
                                DateFormat df = new SimpleDateFormat("MM.dd.yyyy");
                                Date today = Calendar.getInstance().getTime();
                                String reportDate = df.format(today);
                                String fileName="Cierre"+reportDate+".txt";
                                createFile(dayItemsPurchased, fileName,false);
                            }
                        }
                    }
                    System.out.println("Venta capturada, puede seguir o presionar 'q' para salir. "); //Le recuerda al usuario
                    error=false;  //Como fue exitoso, no se repetira el proceso.
                    itemBuy = ing.nextLine();  //Se vuelve a recibir el formato de compra o "q"
                }
                dayEarnings+=sale;  //Se agrega la venta a la venta total del día
                System.out.println("La venta fue exitosa, el total es: "+sale); //Despliega el total
                createFile(itemInfo, "inventory.txt",true); //Actualiza el inventario
                Menu(); //Regresa al menú
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                System.out.println("No se pudo encontrar el producto, vuelva a ingresarlo: "); //En caso de que no encuentre el producto
                itemBuy=ing.nextLine();
            }
        }
    }
    public static void AddToTicket(String[] itemInfo, int amount,int nOfPurch) //Se encarga de crear el ticket. Puede ser llamado múltiples veces
    {
        String[][] ticket=new String [1][3];  //Crea un arreglo que tendrá la información necesaria para el ticket
        ticket[0][0]=""+amount; //Cantidad comprada
        ticket[0][1]=itemInfo[2]; //Nombre
        ticket[0][2]=String.valueOf(amount*Integer.parseInt(itemInfo[3])); //Costo total del producto (cantidad * precio)
        dayItemsPurchased=ticket[0][0]+" "+ticket[0][1]+" "+" "+ticket[0][2]; //Agrega a la lista de productos que se compraron en el día
        DateFormat df = new SimpleDateFormat("MM.dd.yyyy"); //Se formatea y consigue la fecha
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        String fileName=reportDate+"("+nOfPurch+")"+".txt"; //Se utiliza nOfPurch para diferenciar los tickets del día
        createFile(ticket,fileName, false); //Y se manda la información para crear el ticket
    }

    public static String[][] loadInventory() //Carga el inventario de inventory.txt
    {
        String[][] inventoryArray = null; //Se inicializa
        ArrayList<ArrayList<String>> Yinventory = new ArrayList<ArrayList<String>>(); //Se crea una ArrayList bidimensional
        try
        {
            File file = new File("inventory.txt"); //Se instancia File, con el nombre de inventory.txt
            try (Scanner scan = new Scanner(file)) //Se utiliza Scanner para leer el archivo
            {
                scan.useDelimiter("\\|");   //Utilizamos Scanner para utilizar la función del delimitador
                while (scan.hasNext())      //Si lee que tiene el símbolo "|"
                {
                    for(int counter=0;counter<3;counter++)
                    {
                        try
                        {
                            ArrayList<String> Xinventory = new ArrayList<String>(); //Crea un ArrayList unidimensional, serían las "x" del eje
                            Xinventory.add(scan.next()); //Agrega Clave,cantidad,descripción y precio.
                            Xinventory.add(scan.next());
                            Xinventory.add(scan.next());
                            Xinventory.add(scan.next());
                            Yinventory.add(counter, Xinventory);//Agrega a la lista bidimensional la lista unidimensional
                        }
                        catch (NoSuchElementException e) //Si encuentra que ya no hay mas elementos
                        {
                            counter=4;  //Fuerza la salida del ciclo
                        }
                    }
                }
                scan.close(); //Cierra el Scanner
            }
            inventoryArray = new String[Yinventory.size()][4]; //le da propiedades al arreglo, la cantidad de datos que leimos * 4 características
            for (int i=0; i < Yinventory.size(); i++)  //Recorre el arreglo
            {
                ArrayList<String> row = Yinventory.get(i);  //Consigue cada fila
                inventoryArray[i] = row.toArray(new String[row.size()]);  //Y rellena las filas del arreglo.
            }
            return inventoryArray; //Se regresa un arreglo en lugar de un ArrayList
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
    public static void close () //Realiza el cierre
    { 
        System.out.println("Se realizará el cierre del día"); //Imprime mensaje
        System.out.println("********************************");
        System.out.println("Las compras del día de hoy fueron:"); 
        DateFormat df = new SimpleDateFormat("MM.dd.yyyy"); //Consigue el día de hoy con formato
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        String fileName="Cierre"+reportDate+".txt"; 
        showFile(fileName); //Y enseña el archivo de cierre
        System.out.println("********************************");
        System.out.println("Las ganancias del día de hoy son: "+dayEarnings); //Imprime la variable global
        System.out.println("¡Gracias por usar el programa!");
        System.exit(0); //Sale
    }
    public static void showFile(String fileName) //Se encarga de leer e imprimir el texto de los archivos.
    {
        File file = new File (fileName); 
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
