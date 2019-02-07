import javax.smartcardio.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws CardException {
        TerminalFactory terminalFactory = TerminalFactory.getDefault();
        List<CardTerminal> terminals = terminalFactory.terminals().list();
        System.out.println("Terminal: " + terminals);
        CardTerminal terminal = terminals.get(0);
        boolean card = false;
        while (!card) {
            try {
                System.out.println("no");
                if (terminal.isCardPresent()) {
                    System.out.println("yes");
                    Card card1 = terminal.connect("T=0");
                    System.out.println("card: " + card1);
                    byte[] b = hexStringToByteArray("00A4040007A0000002471001");
                    CommandAPDU commandAPDU = new CommandAPDU(b);
                    System.out.println(card1.getBasicChannel());
                    ResponseAPDU responseAPDU = card1.getBasicChannel().transmit(commandAPDU);
                    System.out.println(responseAPDU.toString());
                    card1.disconnect(false);

                }
            } catch (Exception e) {
                card = true;
                e.printStackTrace();
            }
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
