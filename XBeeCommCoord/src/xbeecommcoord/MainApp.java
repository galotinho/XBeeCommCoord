package xbeecommcoord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 *   - Unicast: NODE_IDENTIFIER: message
 *   - Broadcast: ALL: message
*/
public class MainApp {
	
    public static void main(String[] args) throws InterruptedException, ExecutionException {
       /* 
        Thread t1 = new Thread(new ThreadPortaCom("END_DEVICE4", "liga", "COM12", 9600));
        Thread t2 = new Thread(new ThreadPortaCom("END_DEVICE4", "desliga", "COM12", 9600));
        Thread t3 = new Thread(new ThreadPortaCom("END_DEVICE4", "liga", "COM12", 9600));
        Thread t4 = new Thread(new ThreadPortaCom("END_DEVICE4", "desliga", "COM12", 9600));
        
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        
        t1.join(); 
        t2.join();
        t3.join();
        t4.join();
               
        System.exit(0);
        
       
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        List<Callable<String>> lst = new ArrayList<Callable<String>>();
        lst.add(new ThreadPortaComRetorno("END_DEVICE4", "status", "COM12", 9600));
        
        List<Future<String>> tasks = executorService.invokeAll(lst);
        for(Future<String> task : tasks)
        {
          System.out.println(task.get());
        }
        executorService.shutdown();
        
        System.exit(0);
               */
        Double resultado; //variável que guarda o retorno enviado pelo dispositivo.
        String comando = "status";
        //Cria uma pool de threads e adiciona a Thread para fazer uso da porta serial.
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        List<Callable<String>> lst = new ArrayList<>();
        lst.add(new ThreadPortaComRetorno("END_DEVICE1", comando, "COM12", 9600));
        //Executa a Thread e espera o resultado de retorno.
        List<Future<String>> tasks = executorService.invokeAll(lst);
        // Atribui-se o valor lido no dispositivo à variável Resultado.
     
        System.out.println(tasks.get(0).get());
        //Finaliza a pool de Threads.
        executorService.shutdown();
        System.exit(0);
        
    }
}
