import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.darksnakex.villagerfollow.VillagerFollow;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainTest {

    World world;

    @BeforeEach
    void setUp() {
        ServerMock mock = MockBukkit.mock();
        VillagerFollow plugin = MockBukkit.load(VillagerFollow.class);
        this.world = mock.addSimpleWorld("test");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }


    @Test
    void testWorld() {
        assertNotNull(world);

        Location location = new Location(world, 0, 0, 0);

        assertEquals(world, location.getWorld());
        assertEquals(0, location.getBlockX());
        assertEquals(0, location.getBlockY());
        assertEquals(0, location.getBlockZ());
    }
}