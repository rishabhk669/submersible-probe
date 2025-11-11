package probe.submersible.demo.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import probe.submersible.demo.config.SubmersibleException;
import probe.submersible.demo.model.BaseResponse;
import probe.submersible.demo.model.SubmersibleInfo;

import java.util.StringJoiner;

@Slf4j
@Service
public class SubmersibleService {
    @Getter
    private SubmersibleInfo info;

    private StringJoiner sj = new StringJoiner("/n");

    public BaseResponse<Void> initialSetup(SubmersibleInfo info) {
        try {
            validate(info);
        } catch (SubmersibleException se) {
            log.error(se.getMessage());
            return new BaseResponse<>(se.getMessage(), "400", null);
        }
        this.info = info;
        sj.add("stating at position of [" + info.getStartX() + ", " + info.getStartY() + "]");
        return new BaseResponse<>("SUCCESS");
    }

    public BaseResponse<Void> move(String dir) {
        int[] xy = new int[]{0,0};
        try {
            validateMove(dir, xy);
        } catch (SubmersibleException se) {
            log.error(se.getMessage());
            return new BaseResponse<>(se.getMessage(), "400", null);
        }

        log.info("New position {} {}", xy[0], xy[1]);
        info.setStartX(xy[0]);
        info.setStartY(xy[1]);
        sj.add("Moved to position of [" + info.getStartX() + ", " + info.getStartY() + "]");

        return new BaseResponse<>("Moved to " + info.getFacing());
    }


    public BaseResponse<Void> changeDir(String facing) {
        facing = facing.toUpperCase();
        switch (facing) {
            case "E", "W", "N", "S" : break;
            default: return new BaseResponse<>("Invalid facing direction", "400", null);
        }
        info.setFacing(facing);
        sj.add("Changed direction. Facing towards '" + facing +"'");
        return new BaseResponse<>("SUCCESS");
    }

    public BaseResponse<String> getSummary() {
        return new BaseResponse<>("SUCCESS", "200", sj.toString());
    }
    public BaseResponse<Void> reset() {
        info.setStartX(0);
        info.setStartY(0);
        sj.add("Reset to position [0,0]");
        return new BaseResponse<>("SUCCESS");
    }



    public void validate(SubmersibleInfo info) {
        if(info.getGridLength() < 1) throw new SubmersibleException("Grid length must be greater than 0");
        if(info.getGridHeight() < 1) throw new SubmersibleException("Grid height must be greater than 0");
        if(info.getStartX() < 0) throw new SubmersibleException("Start X position cannot be negative");
        if(info.getStartY() < 0) throw new SubmersibleException("Start Y position cannot be negative");
        if(info.getFacing() == null) throw new SubmersibleException("Invalid facing direction");
        info.setFacing(info.getFacing().toUpperCase());
        switch (info.getFacing()) {
            case "E", "W", "N", "S" : break;
            default: throw new SubmersibleException("Invalid facing direction");
        }
        info.getObstacles().stream()
            .filter(o -> (o.x() == info.getStartX() && o.y() == info.getStartY()) // Obstacles and Submersible cannot be the same position
                || (o.x() < 0 || o.y() < 0) //Obstacles cannot on -ve position
                || (o.x() > info.getGridLength() || o.y() > info.getGridHeight()) // Obstacles cannot be outside the grid
            )
            .findAny()
            .ifPresent(o -> {
                log.error("Invalid obstacle position {} {}", o.x(), o.y());
                throw new SubmersibleException("Invalid obstacle position");
            });

    }

    public void validateMove(String dir, int[] xy) {
        switch (info.getFacing()) {
            case "E" : xy[0] = 1; break;
            case "W" : xy[0] = -1; break;
            case "N" : xy[1] = 1; break;
            case "S" : xy[1] = -1; break;
        }
        if(dir.equalsIgnoreCase("F")) {
            xy[0] += info.getStartX();
            xy[1] += info.getStartY();
        } else if(dir.equalsIgnoreCase("B")) {
            xy[0] = info.getStartX() - xy[0];
            xy[1] = info.getStartY() - xy[1];
        } else {
            log.error("Invalid Direction {} to Move", dir);
            sj.add("Invalid move in Direction " + dir);
            throw new SubmersibleException("Invalid Direction to Move");
        }
        info.getObstacles().stream()
                .filter(o -> (o.x() == xy[0] && o.y() == xy[1]) )// Obstacles and Submersible cannot be the same position
                .findAny()
                .ifPresent(o -> {
                    log.error("Obstacle encountered at {} {}", o.x(), o.y());
                    sj.add("Obstacle encountered at [" + o.x() + ", " + o.y() + "]");
                    throw new SubmersibleException("Cannot move. Obstacle encountered");
                });
        if(xy[0] < 0 || xy[0] > info.getGridLength()) {
            log.error("Cannot move to X ({}) of Grid as it ends at {}", xy[0], info.getGridLength());
            sj.add("Cannot move. At edge of X");
            throw new SubmersibleException("Cannot move. At edge of X");
        }
        if(xy[1] < 0 || xy[1] > info.getGridHeight()) {
            log.error("Cannot move to Y ({}) of Grid as it ends at {}", xy[1], info.getGridHeight());
            sj.add("Cannot move. At edge of Y");
            throw new SubmersibleException("Cannot move. At edge of Y");
        }
    }

}
