import javax.mail.*
import java.util.Properties



fun ReadMails(){
    // Nastavení vlastností pro připojení k emailovému serveru
    val properties = Properties()
    properties["mail.store.protocol"] = "imaps"
    properties["mail.imaps.host"] = "imap.forpsi.com"
    properties["mail.imaps.port"] = "993"
    properties["mail.imaps.ssl.enable"] = "true"
    properties["mail.imaps.ssl.trust"] = "*"
    try {
        // Připojení k emailovému serveru
        val session: Session = Session.getDefaultInstance(properties)
        val store: Store = session.getStore("imaps")
        store.connect("vasek@vasekdoskar.cz", "Q5fVk_fWFf")

        // Získání složky INBOX
        val folder: Folder = store.getFolder("INBOX")
        folder.open(Folder.READ_ONLY)

        // Získání pole zpráv
        val messages: Array<Message> = folder.messages

        // Procházení zpráv
        for (message in messages) {
            var obsah = ""
//            val from = message.getFrom()
//            val sub = message.getSubject()
            val content = message.getContent()
            if (content is String) obsah = content else if (content is Multipart) {
                val multipart: Multipart = content
                val partCount: Int = multipart.getCount()
                for (i in 0 until partCount) {
                    val part: BodyPart = multipart.getBodyPart(i)
                    if (part.isMimeType("text/plain")) {
                        obsah = "Content: " + part.getContent()
                    }
                }
            }
            System.out.println("From: " + message.getFrom().get(0))
            System.out.println("Subject: " + message.getSubject())
            println("Content: $obsah")
            println("--------------------------")
        }

        // Uzavření spojení
        folder.close(false)
        store.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun main() {
    ReadMails()
}

