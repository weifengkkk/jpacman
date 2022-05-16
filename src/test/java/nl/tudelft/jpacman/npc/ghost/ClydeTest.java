package nl.tudelft.jpacman.npc.ghost;

import com.google.common.collect.Lists;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClydeTest {
    private PacManSprites pacManSprites;//行走的幽灵
    private BoardFactory boardFactory;//游戏场景
    private GhostFactory ghostFactory;//提供给LevelFactory
    private PointCalculator pointCalculator;
    private LevelFactory levelFactory;
    private GhostMapParser ghostMapParser;//提供快速生成地图的辅助工具类
    private PlayerFactory playerFactory;//用于构造Player
    //给各个类赋初始值
    @BeforeEach
    void init(){
        pacManSprites=new PacManSprites();
        boardFactory=new BoardFactory(pacManSprites);
        ghostFactory=new GhostFactory(pacManSprites);
        pointCalculator=new DefaultPointCalculator();
        levelFactory=new LevelFactory(pacManSprites,ghostFactory,pointCalculator);
        playerFactory=new PlayerFactory(pacManSprites);
        ghostMapParser=new GhostMapParser(levelFactory,boardFactory,ghostFactory);
    }
    @Test
    @DisplayName("可达到小于8")
    void teset1(){
        Level level=ghostMapParser.parseMap(
            Lists.newArrayList(
                "############", "#P       C##", "############"
            )
        );
        //GhostMapParser的parseMap()方法接收上述字符串数组，并返回一个Level对象
        //创建一个player测试对象
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("EAST"));//设置player对象的初始方向
        level.registerPlayer(player);//Level的RegisterPlayer来注册这个新创建的Player对象
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        //调用Navigation的findUnitInBoard()方法来获取
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));
    }
    @Test
    @DisplayName("可达大于8个块")
    void teset2(){
        Level level=ghostMapParser.parseMap(
            Lists.newArrayList(
                "############", "C         P#", "############"
            )
        );
        //GhostMapParser的parseMap()方法接收上述字符串数组，并返回一个Level对象
        //创建一个player测试对象
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("EAST"));//设置player对象的初始方向
        level.registerPlayer(player);//Level的RegisterPlayer来注册这个新创建的Player对象
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        //调用Navigation的findUnitInBoard()方法来获取
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));
    }
    @Test
    @DisplayName("不可达")
    void test3(){
        Level level=ghostMapParser.parseMap(
            Lists.newArrayList(
                "######", "#P##C ", " ###  "
            )
        );
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("EAST"));//设置player对象的初始方向
        level.registerPlayer(player);//Level的RegisterPlayer来注册这个新创建的Player对象
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        //调用Navigation的findUnitInBoard()方法来获取
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
    }
    @Test
    @DisplayName("没有Clyde")
    void test4(){
        Level level=ghostMapParser.parseMap(
            Lists.newArrayList(
                "#####", "##c  ", "     "
            )
        );
        Clyde clyde=Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
        //获得optional类的空对象，代表这个对象不存在

    }
}
