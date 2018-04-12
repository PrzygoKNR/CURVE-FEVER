package CurveFever;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.List;

public class Handling {
   GraphicsContext gc;

   public void handleKeys(List<KeyCode> pressedKeys,Player player) {
         for(KeyCode key: pressedKeys)
         {
            player.handleKey(key);
         }
   }
    
   public Handling(Board board, GraphicsContext gc) {
       this.gc = gc;
   }
}
