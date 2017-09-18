/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Communication.Proxy;

import ComInf.Message;
import ComInf.MessageException;
import Communication.ServerCom;
import genclass.GenericIO;

public class ClientProxy extends Thread
{
  /**
   *  Contador de threads lançados
   *
   *    @serialField nProxy
   */

   private static int nProxy;

  /**
   *  Canal de comunicação
   *
   *    @serialField sconi
   */

   private ServerCom sconi;

  /**
   *  Interface Generic
   *
   *    @serialField genInterface
   */

   private GenericInterface genInterface;

  /**
   *  Instanciação do interface à barbearia.
   *
   *    @param sconi communication channel
     * @param genInterface genInterface
   */

   public ClientProxy (ServerCom sconi, GenericInterface genInterface)
   {
      super ("Proxy_" + getProxyId ());

      this.sconi = sconi;
      this.genInterface = genInterface;
   }

  /**
   *  Life cycle of the agent
   */

   @Override
   public void run ()
   {
      Message inMessage = null,                                      // mensagem de entrada
              outMessage = null;                      // mensagem de saída

      inMessage = (Message) sconi.readObject ();                     // ler pedido do cliente
      try
      { outMessage = genInterface.processAndReply (inMessage);         // processá-lo
      }
      catch (MessageException e)
      { GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
        GenericIO.writelnString (e.getMessageVal ().toString ());
        System.exit (1);
      }
      sconi.writeObject (outMessage);                                // enviar resposta ao cliente
      sconi.close ();                                                // fechar canal de comunicação
   }

  /**
   *  Geração do identificador da instanciação.
   *
   *    @return identificador da instanciação
   */

   private static int getProxyId ()
   {
      Class<Communication.Proxy.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
                                                           //   virtual de Java
      int proxyId;                                         // identificador da instanciação

      try
      { cl = (Class<Communication.Proxy.ClientProxy>) Class.forName ("Communication.Proxy.ClientProxy");
      }
      catch (ClassNotFoundException e)
      { GenericIO.writelnString ("O tipo de dados ClientProxy não foi encontrado!");
        System.exit (1);
      }

      synchronized (cl)
      { proxyId = nProxy;
        nProxy += 1;
      }

      return proxyId;
   }
}
