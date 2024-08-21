package zw.co.fasoft;

import lombok.Data;

import java.util.List;
@Data
public class GroupDetails {

    private String id;
    private String name;
    private String path;
    private List<GroupDetails> subGroups;
}
