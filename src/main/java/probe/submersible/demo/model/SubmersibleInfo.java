package probe.submersible.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmersibleInfo {

    private int gridLength;
    private int gridHeight;
    private int startX;
    private int startY;
    private String facing; //E,W,N,S
    private List<Obstacles> obstacles;

    public record Obstacles(int x, int y) {}

}
