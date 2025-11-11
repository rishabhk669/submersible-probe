package probe.submersible.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmersibleInfo {

    private int gridLength; //x-axis
    private int gridHeight; //y-axis
    private int startX;
    private int startY;
    private String facing; //E,W,N,S
    private Set<Obstacles> obstacles; //Eliminating duplicate

    public Set<Obstacles> getObstacles() {
        if(obstacles == null) obstacles = new HashSet<>();
        return obstacles;
    }

    public record Obstacles(int x, int y) {
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Obstacles obstacles)) return false;
            return x == obstacles.x && y == obstacles.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
