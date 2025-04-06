package shiinatakimod.orbs;

public abstract class AbstractStressOrb extends ShiinaTakiOrb{
    public AbstractStressOrb(
            String ID, String NAME,
            String[] description, String imgPath,
             int passiveAmount,
            int evokeAmount
    ){
        super(ID,NAME,description,imgPath,0,passiveAmount,0,evokeAmount,
                0,0,0,0);
    }
    public AbstractStressOrb(
            String ID, String NAME,
            String[] description, String imgPath,
            int basePassiveAmount, int passiveAmount,
            int baseEvokeAmount, int evokeAmount
    ){
        super(ID,NAME,description,imgPath,basePassiveAmount,passiveAmount,baseEvokeAmount,evokeAmount,
                0,0,0,0);
    }
    public AbstractStressOrb(
            String ID, String NAME,
            String[] description, String imgPath,
            int basePassiveAmount, int passiveAmount,
            int baseEvokeAmount, int evokeAmount,
            int baseOnChannelAmount, int baseOnRemoveAmount,
            int baseOnStartAmount, int baseOnEndAmount
    ){
        super(ID,NAME,description,imgPath,basePassiveAmount,passiveAmount,baseEvokeAmount,evokeAmount,baseOnChannelAmount,baseOnRemoveAmount,baseOnStartAmount,baseOnEndAmount);
    }
}
