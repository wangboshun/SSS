using QRCoder;

using System.Drawing;

namespace SSS.Infrastructure.Util.QRCode
{
    /// <summary>
    /// QrCodeEx
    /// </summary>
    public class QrCodeEx
    {
        private static readonly object _lock = new object();
        private static QrCodeEx _singleton;

        public static QrCodeEx Instance()
        {
            lock (_lock)
            {
                if (_singleton == null) _singleton = new QrCodeEx();
            }

            return _singleton;
        }

        /// <summary>
        /// GetQRCode
        /// </summary>
        /// <param name="content"></param>
        /// <param name="size"></param>
        /// <returns></returns>
        public Bitmap GetQRCode(string content, int size)
        {
            var generator = new QRCodeGenerator();
            var codedata = generator.CreateQrCode(content, QRCodeGenerator.ECCLevel.M, true);
            var qrcode = new QRCoder.QRCode(codedata);
            var img = qrcode.GetGraphic(size);
            return img;
        }
    }
}