package infuzion.chat.server.permission.infuzion.chat.server.permission;

import infuzion.chat.server.permission.Permission;
import infuzion.chat.server.permission.PermissionAttachment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sri on 11/21/2016.
 */
public class PermissionAttachmentTest {
    @Test
    public void addPermission() throws Exception {
        PermissionAttachment pA = new PermissionAttachment();

        Permission permission1 = new Permission("1");
        pA.addPermission(permission1);
        assertTrue(pA.getPermissions().contains(permission1));
    }

    @Test
    public void addPermissions() throws Exception {
        Permission[] permissions = new Permission[5];
        permissions[0] = new Permission("1");
        permissions[1] = new Permission("2");
        permissions[2] = new Permission("3");
        permissions[3] = new Permission("4");
        permissions[4] = new Permission("5");

        List<Permission> permissionList1 = new ArrayList<>();
        permissionList1.add(permissions[0]);
        permissionList1.add(permissions[1]);
        permissionList1.add(permissions[2]);
        permissionList1.add(permissions[3]);
        PermissionAttachment check1 = new PermissionAttachment(permissionList1);

        PermissionAttachment pA1 = new PermissionAttachment();
        pA1.addPermission(permissions[0]);
        pA1.addPermission(permissions[1]);
        PermissionAttachment pA2 = new PermissionAttachment();
        pA2.addPermission(permissions[2]);
        pA2.addPermission(permissions[3]);
        pA1.addPermissions(pA2);

        assertEquals(check1.getPermissions(), pA1.getPermissions());
    }

    @Test
    public void getPermissions() throws Exception {
        PermissionAttachment pA = new PermissionAttachment();
        assertEquals(0, pA.getPermissions().size());
        pA.addPermission(new Permission("123"));
        assertEquals(1, pA.getPermissions().size());
        for (int x = 0; x < 100; x++) {
            pA.addPermission(new Permission("permission." + x));
        }
        assertEquals(101, pA.getPermissions().size());
    }

    @Test
    public void containsPermission() throws Exception {
        PermissionAttachment pA = new PermissionAttachment();
        pA.addPermission(new Permission("123"));
        assertTrue(pA.containsPermission(new Permission("123")));
    }

    @Test
    public void iterator() throws Exception {
        PermissionAttachment pA = new PermissionAttachment();
        Permission[] permissions = new Permission[5];
        permissions[0] = new Permission("1");
        permissions[1] = new Permission("2");
        permissions[2] = new Permission("3");
        permissions[3] = new Permission("4");
        permissions[4] = new Permission("5");
        for (Permission p : permissions) {
            pA.addPermission(p);
        }
        Iterator iterator = pA.iterator();
        int counter = 0;
        assertTrue(iterator.hasNext());
        while (iterator.hasNext()) {
            assertEquals(iterator.next(), permissions[counter]);
            counter++;
        }
        assertEquals(5, counter);
    }

}