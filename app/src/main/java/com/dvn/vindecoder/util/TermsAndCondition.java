package com.dvn.vindecoder.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

/**
 * Created by palash on 19-06-2016.
 */

public class TermsAndCondition {

    private String EULA_PREFIX = "eula_";
    private Activity mActivity;

    public TermsAndCondition(Activity context) {
        mActivity = context;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    public void show() {
        PackageInfo versionInfo = getPackageInfo();

        // the eulaKey changes every time you increment the version number in the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean hasBeenShown = prefs.getBoolean(eulaKey, false);
       // if(hasBeenShown == false)
        {

            // Show the Eula
            String title = "Terms of Use";

            //Includes the updates as well so users know what changed.
            //String message = //mActivity.getString(R.string.eula);
            String message ="Welcome to SmartGrowth.in, a perspectful Advisory services company for parents. In using\n" +
                                         "\n" +
                                         "the SmartGrowth service, you are expected to have follow the Terms and Conditions\n" +
                                         "\n" +
                                         "(‘Terms’) of the agreement listed below:\n" +
                                         "\n" +
                                         "I. Listing Account\n" +
                                         "\n" +
                                         "SmartgGrowth is offering a Listing account that enables the Client to book the service from listing\n" +
                                         "\n" +
                                         "services (“SmartGrowth listing”) through SmartGrowth’s websites, mobile applications, and other\n" +
                                         "\n" +
                                         "properties. Each such order (‘Purchase Order’) will be agreed in writing by both parties which\n" +
                                         "\n" +
                                         "sets forth the SmartGrowth listings being purchased, applicable fees, the duration of the paid\n" +
                                         "\n" +
                                         "listing commitment (the “Commitment Period”), renewal terms, and the date that paid listing will\n" +
                                         "\n" +
                                         "commence, among other information (“Purchase Order”). Each booking is governed by and\n" +
                                         "\n" +
                                         "incorporated into the Terms, and any disputes between them will be resolved by them.\n" +
                                         "\n" +
                                         "II. Smartgrowth Listing\n" +
                                         "\n" +
                                         "Smartgrowth Listing encompass a range of different advertising programs.\n" +
                                         "\n" +
                                         "The “Starter Kit” allows Client to access advanced features in connection with its business profile\n" +
                                         "\n" +
                                         "page, such as posting multiple programs, photo &amp; video, and dedicated account support. As it\n" +
                                         "\n" +
                                         "develops new advertising features, Smartgrowth may from time to time replace any of the\n" +
                                         "\n" +
                                         "foregoing features with features of substantially similar value.\n" +
                                         "\n" +
                                         "III. Fees and Payment\n" +
                                         "\n" +
                                         "Client will pay Smartgrowth the fees specified in each Purchase Order for the duration of its term.\n" +
                                         "\n" +
                                         "The fees are fixed for the duration of the commitment period specified in the applicable Purchase\n" +
                                         "\n" +
                                         "Order, but may be modified by Smartgrowth thereafter by providing one month’s prior written\n" +
                                         "\n" +
                                         "notice to Client. The fees are net of any taxes Client may be required to pay in its taxing\n" +
                                         "\n" +
                                         "jurisdiction. Payments are due in advance of the period for which they apply, or as otherwise set\n" +
                                         "\n" +
                                         "forth in the Purchase Order. Unpaid amounts or errors may be billed in subsequent invoices. If\n" +
                                         "\n" +
                                         "Client’s payment method fails or Client’s account is past due, Smartgrowth may collect past due\n" +
                                         "\n" +
                                         "amounts using other collection mechanisms, and Client agrees to pay all expenses associated\n" +
                                         "\n" +
                                         "with such collection, including reasonable attorneys’ fees.\n" +
                                         "\n" +
                                         "Cancellation/Refund Policy: While availing any of the payment method/s available on the\n" +
                                         "\n" +
                                         "Website, Smartgrowth will not be responsible or assume any liability whatsoever in respect of\n" +
                                         "\n" +
                                         "any loss or damage arising directly or indirectly to you. All payments made on the Website,\n" +
                                         "\n" +
                                         "including payments made toward registration for classes, are non-refundable. In the event\n" +
                                         "\n" +
                                         "payment is deducted from the User&#39;s account with non-completion of the booking, the exact\n" +
                                         "\n" +
                                         "amount shall be refunded to the User via the same mode of payment within 3-5 working days. All\n" +
                                         "\n" +
                                         "commercial terms are on a principal-to- principal basis between the Merchant and the users and\n" +
                                         "\n" +
                                         "Smartgrowth’s payment facility is merely used to facilitate the completion of the transaction.\n" +
                                         "\n" +
                                         "Transaction charges, not limited to payment gateway charges, shall be applicable, unless\n" +
                                         "\n" +
                                         "explicitly waived off by Smartgrowth.\n" +
                                         "\n" +
                                         "IF CLIENT PROVIDES Smartgrowth WITH CREDIT CARD, DEBIT CARD, OR BANK ACCOUNT\n" +
                                         "\n" +
                                         "INFORMATION, CLIENT AUTHORIZES Smartgrowth TO USE SUCH PAYMENT\n" +
                                         "\n" +
                                         "INFORMATION TO AUTOMATICALLY CHARGE CLIENT ON A RECURRING BASIS TO\n" +
                                         "\n" +
                                         "COLLECT ALL FEES DUE HEREUNDER. CLIENT REPRESENTS THAT HE OR SHE IS\n" +
                                         "\n" +
                                         "AUTHORIZED TO INCUR CHARGES AGAINST THE PAYMENT CARD USED TO PURCHASE\n" +
                                         "\n" +
                                         "Smartgrowth ADS. THE FORM OF PAYMENT CANNOT BE CHANGED OR ALTERED UNLESS\n" +
                                         "\n" +
                                         "ALL AMOUNTS DUE UNDER THE TERMS HAVE BEEN PAID IN FULL OR OTHERWISE\n" +
                                         "\n" +
                                         "AGREED TO BY THE PARTIES IN WRITING.\n" +
                                         "\n" +
                                         "IV. Representations and Warranties\n" +
                                         "\n" +
                                         "Each party represents and warrants to the other that it is duly organized, validly existing, and in\n" +
                                         "\n" +
                                         "good standing under the laws of the jurisdiction in which it was organized; all contact and entity\n" +
                                         "\n" +
                                         "information is complete, correct and current, and the execution and delivery of the Terms, and\n" +
                                         "\n" +
                                         "the performance of the transactions contemplated hereby, are within its corporate powers, and\n" +
                                         "\n" +
                                         "have been duly authorized by all necessary corporate action.\n" +
                                         "\n" +
                                         "Client represents and warrants to Smartgrowth that any information or materials that Client\n" +
                                         "\n" +
                                         "provides in connection with Smartgrowth listings will (a) be true and complete, (b) not contain any\n" +
                                         "\n" +
                                         "material which violates Smartgrowth’s content guidelines or which is otherwise unlawful,\n" +
                                         "\n" +
                                         "defamatory or obscene, or which infringes or violates any third-party rights (including any\n" +
                                         "\n" +
                                         "intellectual property rights or privacy or publicity rights) or which may encourage a criminal\n" +
                                         "\n" +
                                         "offense or otherwise give rise to civil liability and (c) comply with all applicable laws and\n" +
                                         "\n" +
                                         "regulations in its performance of the Terms (including all applicable privacy / data protection laws\n" +
                                         "\n" +
                                         "and regulations and laws related to Promotions). “Promotions” are any contest, sweepstakes,\n" +
                                         "\n" +
                                         "coupon or other promotion appearing on or promoted through the Site by Client. Smartgrowth\n" +
                                         "\n" +
                                         "reserves the right to reject or remove any Advertising Materials at its sole discretion, and to alter\n" +
                                         "\n" +
                                         "any Advertising Materials to conform to technical specifications.\n" +
                                         "\n" +
                                         "Client further represents and warrants to Smartgrowth that Client will not, and will not authorize\n" +
                                         "\n" +
                                         "or induce any other party, to: (a) generate automated, fraudulent or otherwise invalid inquiries,\n" +
                                         "\n" +
                                         "conversions or other actions; (b) use any automated means or form of scraping or data extraction\n" +
                                         "\n" +
                                         "to access, query or otherwise collect Smartgrowth content and reviews from the Site, except as\n" +
                                         "\n" +
                                         "expressly permitted by Smartgrowth or (c) use any Smartgrowth trademarks in any manner\n" +
                                         "\n" +
                                         "without Smartgrowth’s prior written consent. All rights not expressly granted to Client hereunder\n" +
                                         "\n" +
                                         "are reserved by Smartgrowth.\n" +
                                         "\n" +
                                         "V. Information About and Use of the Site\n" +
                                         "\n" +
                                         "The Site allows consumers to post reviews about businesses like Client’s. The Site employs\n" +
                                         "\n" +
                                         "automated software to help it showcase the most relevant and reliable reviews while suppressing\n" +
                                         "\n" +
                                         "others. Client’s purchase of Smartgrowth Ads will not influence the automated software, or\n" +
                                         "\n" +
                                         "otherwise allow or enable Client, directly or indirectly, to remove, alter or reorder the reviews on\n" +
                                         "\n" +
                                         "the Site.\n" +
                                         "\n" +
                                         "VI. Termination\n" +
                                         "\n" +
                                         "Each Purchase Order will expire at the end of the Commitment Period unless (i) otherwise\n" +
                                         "\n" +
                                         "terminated earlier under these Terms or (ii) the Purchase Order contains a renewal period that\n" +
                                         "\n" +
                                         "automatically extends the term of the Purchase Order beyond the Commitment Period\n" +
                                         "\n" +
                                         "(collectively the “PO Term”). Client must provide written notice (an email to Client’s assigned\n" +
                                         "\n" +
                                         "Smartgrowth account representative is permissible) to terminate a Purchase Order on or before\n" +
                                         "\n" +
                                         "the 15th day of the month in order for the Purchase Order to terminate at the end of that month. If\n" +
                                         "\n" +
                                         "Client provides written notice after the 15th day of the month, then the Purchase Order will\n" +
                                         "\n" +
                                         "terminate at the end of the following month. Unless otherwise terminated as provided under the\n" +
                                         "\n" +
                                         "Terms, the Terms will automatically expire three (3) months after the termination or expiration of\n" +
                                         "\n" +
                                         "the last surviving Purchase Order.\n" +
                                         "\n" +
                                         "IF CLIENT TERMINATES A PURCHASE ORDER BEFORE THE END OF THE COMMITMENT\n" +
                                         "\n" +
                                         "PERIOD, CLIENT AGREES TO PAY ANY AND ALL EARLY TERMINATION FEES SET FORTH\n" +
                                         "\n" +
                                         "IN THE PURCHASE ORDER(S), RECOGNIZING THAT Smartgrowth BEARS CERTAIN UP-\n" +
                                         "\n" +
                                         "FRONT COSTS, AND THAT THERE IS AN IMPLIED PRICING DISCOUNT BASED ON THE\n" +
                                         "\n" +
                                         "LENGTH OF CLIENT’S CHOSEN COMMITMENT PERIOD.\n" +
                                         "\n" +
                                         "Smartgrowth may terminate any Purchase Order or the Terms at any time for any or no reason\n" +
                                         "\n" +
                                         "without liability, effective immediately, by providing written notice to Client. In the event of such\n" +
                                         "\n" +
                                         "termination, Client will immediately pay all unpaid Smartgrowth Ad fees through the date of\n" +
                                         "\n" +
                                         "termination, and Smartgrowth will reimburse any fees that were prepaid for Smartgrowth Ads to\n" +
                                         "\n" +
                                         "be rendered after the date of such termination.\n" +
                                         "\n" +
                                         "VII. Smartgrowth’S DISCLAIMER OF WARRANTIES\n" +
                                         "\n" +
                                         "CLIENT ACKNOWLEDGES AND AGREES THAT Smartgrowth SERVICES ARE PROVIDED TO\n" +
                                         "\n" +
                                         "CLIENT ON AN “AS IS”, “WITH ALL FAULTS” AND “AS AVAILABLE” BASIS. Smartgrowth\n" +
                                         "\n" +
                                         "MAKES NO WARRANTIES, EITHER EXPRESS OR IMPLIED, ABOUT THE Smartgrowth\n" +
                                         "\n" +
                                         "LISTING AND EXPRESSLY DISCLAIMS THE WARRANTY OF MERCHANTABILITY AND\n" +
                                         "\n" +
                                         "WARRANTY OF FITNESS FOR A PARTICULAR PURPOSE. FURTHERMORE, TO THE\n" +
                                         "\n" +
                                         "FULLEST EXTENT PERMITTED BY LAW, Smartgrowth SPECIFICALLY DISCLAIMS ALL\n" +
                                         "\n" +
                                         "WARRANTIES AND GUARANTEES REGARDING (I) THE PERFORMANCE, QUALITY AND\n" +
                                         "\n" +
                                         "RESULTS FOR THE SERVICE, (II) PAGE VIEWS, CONVERSIONS OR OTHER\n" +
                                         "\n" +
                                         "PERFORMANCE OR RESULTS FOR THE SERVICE, (III) THE ACCURACY OF THE\n" +
                                         "\n" +
                                         "INFORMATION THAT Smartgrowth PROVIDES IN CONNECTION WITH THE SITE OR\n" +
                                         "\n" +
                                         "Smartgrowth LISTING (E.G. REACH, SIZE OF AUDIENCE, DEMOGRAPHICS OR OTHER\n" +
                                         "\n" +
                                         "PURPORTED CHARACTERISTICS OF AUDIENCE), (IV) Smartgrowth’S ABILITY TO TARGET\n" +
                                         "\n" +
                                         "ADS TO OR IN CONNECTION WITH SPECIFIC USERS, TYPES OF USERS, USER QUERIES,\n" +
                                         "\n" +
                                         "OR OTHER USER BEHAVIORS. Smartgrowth SHALL NOT BE LIABLE FOR NON-\n" +
                                         "\n" +
                                         "PERFORMANCE DUE TO CAUSES BEYOND ITS REASONABLE CONTROL.\n" +
                                         "\n" +
                                         "VIII. LIMITATIONS OF LIABILITY\n" +
                                         "\n" +
                                         "FOR ALL CLAIMS ARISING FROM OR IN CONNECTION WITH A PURCHASE ORDER,\n" +
                                         "\n" +
                                         "Smartgrowth LISTING, OR THE TERMS THAT ARE NOT EXPRESSLY ADDRESSED IN THIS\n" +
                                         "\n" +
                                         "SECTION TITLED “LIMITATIONS OF LIABILITY”, Smartgrowth’S MAXIMUM LIABILITY AND\n" +
                                         "\n" +
                                         "CLIENT’S EXCLUSIVE REMEDY IS THE AGGREGATE FEES PAYABLE TO Smartgrowth\n" +
                                         "\n" +
                                         "HEREUNDER DURING THE SPECIFIED COMMITMENT PERIOD EXCEPT WHERE AND TO\n" +
                                         "\n" +
                                         "THE EXTENT PROHIBITED BY APPLICABLE LAW.\n" +
                                         "\n" +
                                         "NEITHER PARTY NOR ITS AFFILIATES WILL BE LIABLE FOR ANY INDIRECT, INCIDENTAL,\n" +
                                         "\n" +
                                         "CONSEQUENTIAL, SPECIAL, OR EXEMPLARY DAMAGES (INCLUDING LOSS OF PROFITS\n" +
                                         "\n" +
                                         "OR REVENUE, OR INTERRUPTION OF BUSINESS) ARISING OUT OF OR RELATED TO A\n" +
                                         "\n" +
                                         "PURCHASE ORDER, THE Smartgrowth LISTING, THE SITE, OR THESE TERMS,\n" +
                                         "\n" +
                                         "REGARDLESS OF THE THEORY OF LIABILITY, EVEN IF A PARTY HAS BEEN ADVISED OF\n" +
                                         "\n" +
                                         "THE POSSIBILITY OF SUCH DAMAGES.\n" +
                                         "\n" +
                                         "Smartgrowth does not represent in any manner that:\n" +
                                         "\n" +
                                         "● The information, data or contents of the Website are accurate;\n" +
                                         "\n" +
                                         "● The Website will be available at all times and will operate error free or that there will be\n" +
                                         "\n" +
                                         "uninterrupted access and service;\n" +
                                         "\n" +
                                         "● The integrity of the information on the Website or information you upload will be\n" +
                                         "\n" +
                                         "maintained;\n" +
                                         "\n" +
                                         "● We endorses any of the views of any of the users who may have posted content;\n" +
                                         "\n" +
                                         "● We have verified or guarantee the quality of services or representations made by any\n" +
                                         "\n" +
                                         "user of the Website;\n" +
                                         "\n" +
                                         "● We have verified the credit worthiness of any user;\n" +
                                         "\n" +
                                         "● We have screened or verified any of the information posted herein; and\n" +
                                         "\n" +
                                         "● The Website or any content is free from viruses or other malware.\n" +
                                         "\n" +
                                         "\n" +
                                         "\n" +
                                         "IX. Indemnification\n" +
                                         "\n" +
                                         "Client will indemnify, defend, and hold Smartgrowth and its officers, directors, agents, and\n" +
                                         "\n" +
                                         "employees harmless from and against any and all claims, actions, losses, damages, liabilities,\n" +
                                         "\n" +
                                         "costs and expenses (including but not limited to attorneys’ fees and court costs) (collectively a\n" +
                                         "\n" +
                                         "“Third Party Claim”) arising out of or in connection with (i) the Advertising Materials, Client\n" +
                                         "\n" +
                                         "Instructions, or Client’s use of Smartgrowth Ads, (ii) any breach of representations or warranties\n" +
                                         "\n" +
                                         "provided under these Terms by Client in Section IV, (iii) any Promotion, including any claims for\n" +
                                         "\n" +
                                         "any violation by the Promotion of any applicable law, rule or regulation, (iv) Client’s products or\n" +
                                         "\n" +
                                         "services or the provision thereof to end users. Smartgrowth will notify Client promptly of any\n" +
                                         "\n" +
                                         "Third Party Claim for which it seeks indemnification and will permit Client to control the defense\n" +
                                         "\n" +
                                         "of such Third Party Claim with counsel chosen by Client; provided, that Client will not enter into\n" +
                                         "\n" +
                                         "any settlement that contains any admission of or stipulation to any guilt, fault, liability or\n" +
                                         "\n" +
                                         "wrongdoing on the part of Smartgrowth without Smartgrowth’s prior written consent.\n" +
                                         "\n" +
                                         "X. Force Majeure: In no event shall Smartgrowth be liable for any acts beyond our control or for\n" +
                                         "\n" +
                                         "any acts of god.\n" +
                                         "\n" +
                                         "XI. Choice of Law and Arbitration\n" +
                                         "\n" +
                                         "Any claim, controversy or dispute arising out of or relating to the Terms (“Claim”) shall be settled\n" +
                                         "\n" +
                                         "under the laws of Indian courts subject to laws of Republic of India. In the event of any dispute of\n" +
                                         "\n" +
                                         "whatever nature, such dispute shall be settled in good faith between the parties. In case, such\n" +
                                         "\n" +
                                         "dispute cannot be resolved by negotiation within 30 days, such dispute shall be referred to a\n" +
                                         "\n" +
                                         "binding arbitration in accordance with the provisions of the Arbitration and Conciliation Act 1996.\n" +
                                         "\n" +
                                         "The place of arbitration shall be Delhi, India. In case of the dispute requiring intervention of\n" +
                                         "\n" +
                                         "courts, courts in Delhi, India alone shall have exclusive jurisdiction.\n" +
                                         "\n" +
                                         "XII. In no event shall Smartgrowth be liable for any loss of profits (anticipated or real), loss of\n" +
                                         "\n" +
                                         "business, loss of reputation, loss of data, loss of goodwill, any business interruption or any direct,\n" +
                                         "\n" +
                                         "indirect, special, incidental, consequential, punitive, tort or other damages, however caused,\n" +
                                         "\n" +
                                         "whether or not we have been advised of the possibility of such damages.\n" +
                                         "\n" +
                                         "XIII. Miscellaneous\n" +
                                         "\n" +
                                         "(a) The Terms and its exhibits are expressly limited to and made conditional upon Client’s\n" +
                                         "\n" +
                                         "acceptance of its terms and conditions before the Expiration Date. Any of Client’s terms or\n" +
                                         "\n" +
                                         "conditions which are in addition to or different from those contained in or added by way of\n" +
                                         "\n" +
                                         "interlineation to the Terms or any Purchase Order as originally provided to Client by Smartgrowth\n" +
                                         "\n" +
                                         "which are not separately expressly agreed to in writing by both parties are deemed material and\n" +
                                         "\n" +
                                         "are hereby objected to and rejected by Smartgrowth. No conditions, printed or otherwise,\n" +
                                         "\n" +
                                         "appearing on other contracts, orders or copy instructions which conflict with, vary, or add to these\n" +
                                         "\n" +
                                         "Terms will be binding on Smartgrowth, and any conflicting or additional terms contain in any\n" +
                                         "\n" +
                                         "other documents or oral discussions are void. The Terms embody the entire and exclusive\n" +
                                         "\n" +
                                         "agreement between the parties respecting the subject matter of herein, and supersede any and\n" +
                                         "\n" +
                                         "all prior related oral, emailed or written representations and agreements between the parties. No\n" +
                                         "\n" +
                                         "statements or promises by either party have been relied upon in entering into these Terms,\n" +
                                         "\n" +
                                         "except as expressly set forth herein.\n" +
                                         "\n" +
                                         "(b) Anyone agreeing to the Terms on behalf of Client represents and warrants that it has full legal\n" +
                                         "\n" +
                                         "power and authority to enter into these Terms, perform its obligations hereunder, and authorize\n" +
                                         "\n" +
                                         "the fee payments set forth in the Purchase Order(s).\n" +
                                         "\n" +
                                         "(c) Notices under these Terms must be in writing and sent via facsimile, registered or certified\n" +
                                         "\n" +
                                         "mail or commercial courier to the parties at their respective addresses set forth herein.\n" +
                                         "\n" +
                                         "(d) The Terms may not be amended or modified except as agreed upon in writing by the parties.\n" +
                                         "\n" +
                                         "No provision in the Terms may be waived, except pursuant to a writing executed by the party\n" +
                                         "\n" +
                                         "against whom the waiver is sought to be enforced. Client may not assign any rights or obligations\n" +
                                         "\n" +
                                         "under the Terms without Smartgrowth’s prior consent, and any purported assignment by Client\n" +
                                         "\n" +
                                         "shall be void. If any provision of the Terms is held to be invalid or unenforceable, the parties will\n" +
                                         "\n" +
                                         "substitute for the affected provision a valid or enforceable provision that approximates the intent\n" +
                                         "\n" +
                                         "and economic effect of the affected provision. Sections VII, VIII, IX, X and XI of the Terms will\n" +
                                         "\n" +
                                         "survive any termination of the Term.";
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Mark this version as read.
                            /*SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(eulaKey, true);
                            editor.commit();*/
                            dialogInterface.dismiss();
                        }
                    });
                   /* .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the activity as they have declined the EULA
                            mActivity.finish();
                        }

                    });*/
            builder.create().show();
        }
    }

}