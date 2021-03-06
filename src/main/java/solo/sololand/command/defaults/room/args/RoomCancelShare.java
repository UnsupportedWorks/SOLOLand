package solo.sololand.command.defaults.room.args;

import cn.nukkit.Server;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import solo.sololand.command.SubCommand;
import solo.sololand.world.World;
import solo.sololand.land.Land;
import solo.sololand.land.Room;
import solo.solobasepackage.util.Message;
import solo.solobasepackage.util.Notification;

public class RoomCancelShare extends SubCommand{

	public RoomCancelShare(){
		super("공유취소", "공유중이던 유저의 공유를 취소합니다.");
		this.setPermission("sololand.command.room.cancelshare");
		this.addCommandParameters(new CommandParameter[]{
			new CommandParameter("유저", CommandParameter.ARG_TYPE_STRING, false)
		});
		this.addCommandParameters(new CommandParameter[]{
			new CommandParameter("유저", CommandParameter.ARG_TYPE_STRING, false),
			new CommandParameter("유저", CommandParameter.ARG_TYPE_STRING, true),
			new CommandParameter("유저...", CommandParameter.ARG_TYPE_RAW_TEXT, true)
		});
	}

	@Override
	public boolean execute(CommandSender sender, String[] args){
		Player player = (Player) sender;
		World world = World.get(player);
		Land land = world.getLand(player);
		if(land == null){
			Message.alert(player, "현재 위치에서 땅을 찾을 수 없습니다.");
			return true;
		}
		Room room = land.getRoom(player);
		if(room == null){
			Message.alert(player, "현재 위치에서 방을 찾을 수 없습니다.");
			return true;
		}
		if(!player.isOp() && !land.isOwner(player) && !room.isOwner(player)){
			Message.alert(player, "방 주인이 아니므로 공유목록을 수정할 수 없습니다.");
			return true;
		}
		if(args.length < 1){
			return false;
		}
		for(String arg : args){
			Player target = Server.getInstance().getPlayer(arg);
			String targetName;
			if(target == null){
				targetName = arg.toLowerCase();
			}else{
				targetName = target.getName().toLowerCase();
			}
			if(room.isOwner(targetName)){
				Message.alert(player, "방 주인을 공유 취소할 수 없습니다.");
				continue;
			}
			if (!room.isMember(targetName)){
				Message.alert(player, targetName + "님은 공유 목록에 없습니다.");
				continue;
			}
			room.removeMember(targetName);
			Message.normal(player, targetName + "님을 공유 취소 하였습니다.");
			
			Notification.addNotification(target, world.getCustomName() + " 월드의 " + room.getAddress() + "번 방 공유가 취소되었습니다.");
		}
		return true;
	}
}