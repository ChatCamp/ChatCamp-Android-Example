package com.stfalcon.chatkit.messages;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.ViewHolder;
import com.stfalcon.chatkit.commons.models.IActionContent;
import com.stfalcon.chatkit.commons.models.IActionMessage;
import com.stfalcon.chatkit.commons.models.IActionSubContent;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.stfalcon.chatkit.utils.RoundedImageView;
import com.wang.avi.AVLoadingIndicatorView;

import org.apmem.tools.layouts.FlowLayout;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Created by troy379 on 31.03.17.
 */
@SuppressWarnings("WeakerAccess")
public class MessageHolders {
//
//
//    private static final short VIEW_TYPE_DATE_HEADER = 130;
//    private static final short VIEW_TYPE_TEXT_MESSAGE = 131;
//    private static final short VIEW_TYPE_IMAGE_MESSAGE = 132;
//
//    private Class<? extends ViewHolder<Date>> dateHeaderHolder;
//    private int dateHeaderLayout;
//
//    private HolderConfig<IMessage> incomingTextConfig;
//    private HolderConfig<IMessage> outcomingTextConfig;
//    private HolderConfig<MessageContentType.Image> incomingImageConfig;
//    private HolderConfig<MessageContentType.Image> outcomingImageConfig;
//    private HolderConfig<IMessage> incomingTypingConfig;
//    private HolderConfig<IMessage> incomingTextConfigChatCamp;
//    private HolderConfig<IMessage> outcomingTextConfigChatCamp;
//    private HolderConfig<MessageContentType.Image> incomingImageConfigChatCamp;
//    private HolderConfig<MessageContentType.Image> outcomingImageConfigChatCamp;
//    private HolderConfig<IMessage> incomingActionConfigChatCamp;
//    private HolderConfig<IMessage> outcomingActionConfigChatCamp;
//    private HolderConfig<MessageContentType.Video> incomingVideoConfigChatCamp;
//    private HolderConfig<MessageContentType.Video> outcomingVideoConfigChatCamp;
//    private HolderConfig<MessageContentType.Document> incomingDocumentConfigChatCamp;
//    private HolderConfig<MessageContentType.Document> outcomingDocumentConfigChatCamp;
//
//    private List<ContentTypeConfig> customContentTypes = new ArrayList<>();
//    private ContentChecker contentChecker;
//
//    public void setLastTimeRead(long lastTimeRead) {
//        MessageHolders.lastTimeRead = lastTimeRead;
//    }
//
//    private static long lastTimeRead;
//
//    public void setOnActionItemClickedListener(OnActionItemClickedListener onItemClicked) {
//        onActionItemClickedListener = onItemClicked;
//    }
//
//    public void setOnVideoItemClickedListener(OnVideoItemClickedListener onItemClicked) {
//        onVideoItemClickedListener = onItemClicked;
//    }
//
//    public void setOnDocumentItemClickedListener(OnDocumentItemClickedListener onItemClicked) {
//        onDocumentItemClickedListener = onItemClicked;
//    }
//
//    private static OnActionItemClickedListener onActionItemClickedListener;
//    private static OnVideoItemClickedListener onVideoItemClickedListener;
//    private static OnDocumentItemClickedListener onDocumentItemClickedListener;
//
//    public interface OnActionItemClickedListener {
//        void onActionItemClicked(String url);
//
//        void onActionContentActionClicked(IActionContent actionContent);
//    }
//
//    public interface OnVideoItemClickedListener {
//        void onVideoItemClicked(String url);
//    }
//
//    public interface OnDocumentItemClickedListener {
//        void onDocumentItemClicked(MessageContentType.Document message);
//    }
//
//    public MessageHolders() {
//        lastTimeRead = 0L;
//        this.dateHeaderHolder = DefaultDateHeaderViewHolder.class;
//        this.dateHeaderLayout = R.layout.item_date_header;
//
//        this.incomingTextConfig = new HolderConfig<>(DefaultIncomingTextMessageViewHolder.class, R.layout.item_incoming_text_message);
//        this.outcomingTextConfig = new HolderConfig<>(DefaultOutcomingTextMessageViewHolder.class, R.layout.item_outcoming_text_message);
//        this.incomingImageConfig = new HolderConfig<>(DefaultIncomingImageMessageViewHolder.class, R.layout.item_incoming_image_message);
//        this.outcomingImageConfig = new HolderConfig<>(DefaultOutcomingImageMessageViewHolder.class, R.layout.item_outcoming_image_message);
//        this.incomingTypingConfig = new HolderConfig<>(ChatcampDefaultIncomingTypingMessageViewHolder.class, R.layout.item_incoming_typing_message_chatcamp);
//        this.incomingTextConfigChatCamp = new HolderConfig<>(ChatcampDefaultIncomingTextMessageViewHolder.class, R.layout.item_incoming_text_message_chatcamp);
//        this.outcomingTextConfigChatCamp = new HolderConfig<>(ChatcampDefaultOutcomingTextMessageViewHolder.class, R.layout.item_outcoming_text_message_chatcamp);
//        this.incomingImageConfigChatCamp = new HolderConfig<>(ChatcampDefaultIncomingImageMessageViewHolder.class, R.layout.item_incoming_image_message_chatcamp);
//        this.outcomingImageConfigChatCamp = new HolderConfig<>(ChatcampDefaultOutcomingImageMessageViewHolder.class, R.layout.item_outcoming_image_message_chatcamp);
//        this.incomingActionConfigChatCamp = new HolderConfig<>(ChatcampDefaultIncomingActionMessageViewHolder.class, R.layout.item_incoming_action_message_chatcamp);
//        this.outcomingActionConfigChatCamp = new HolderConfig<>(ChatcampDefaultOutcomingActionMessageViewHolder.class, R.layout.item_outcoming_action_message_chatcamp);
//        this.incomingVideoConfigChatCamp = new HolderConfig<>(ChatcampDefaultIncomingVideoMessageViewHolder.class, R.layout.item_incoming_video_message_chatcamp);
//        this.outcomingVideoConfigChatCamp = new HolderConfig<>(ChatcampDefaultOutcomingVideoMessageViewHolder.class, R.layout.item_outcoming_video_message_chatcamp);
//        this.incomingDocumentConfigChatCamp = new HolderConfig<>(ChatcampDefaultIncomingDocumentMessageViewHolder.class, R.layout.item_incoming_document_message_chatcamp);
//        this.outcomingDocumentConfigChatCamp = new HolderConfig<>(ChatcampDefaultOutcomingDocumentMessageViewHolder.class, R.layout.item_outcoming_document_message_chatcamp);
//    }
//
//    /**
//     * Sets both of custom view holder class and layout resource for incoming text message.
//     *
//     * @param holder holder class.
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setIncomingTextConfig(
//            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder,
//            @LayoutRes int layout) {
//        this.incomingTextConfig.holder = holder;
//        this.incomingTextConfig.layout = layout;
//        return this;
//    }
//
//    /**
//     * Sets custom view holder class for incoming text message.
//     *
//     * @param holder holder class.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setIncomingTextHolder(
//            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder) {
//        this.incomingTextConfig.holder = holder;
//        return this;
//    }
//
//    /**
//     * Sets custom layout resource for incoming text message.
//     *
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setIncomingTextLayout(@LayoutRes int layout) {
//        this.incomingTextConfig.layout = layout;
//        return this;
//    }
//
//    /**
//     * Sets both of custom view holder class and layout resource for outcoming text message.
//     *
//     * @param holder holder class.
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setOutcomingTextConfig(
//            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder,
//            @LayoutRes int layout) {
//        this.outcomingTextConfig.holder = holder;
//        this.outcomingTextConfig.layout = layout;
//        return this;
//    }
//
//    /**
//     * Sets custom view holder class for outcoming text message.
//     *
//     * @param holder holder class.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setOutcomingTextHolder(
//            @NonNull Class<? extends BaseMessageViewHolder<? extends IMessage>> holder) {
//        this.outcomingTextConfig.holder = holder;
//        return this;
//    }
//
//    /**
//     * Sets custom layout resource for outcoming text message.
//     *
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setOutcomingTextLayout(@LayoutRes int layout) {
//        this.outcomingTextConfig.layout = layout;
//        return this;
//    }
//
//    /**
//     * Sets both of custom view holder class and layout resource for incoming image message.
//     *
//     * @param holder holder class.
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setIncomingImageConfig(
//            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder,
//            @LayoutRes int layout) {
//        this.incomingImageConfig.holder = holder;
//        this.incomingImageConfig.layout = layout;
//        return this;
//    }
//
//    /**
//     * Sets custom view holder class for incoming image message.
//     *
//     * @param holder holder class.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setIncomingImageHolder(
//            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder) {
//        this.incomingImageConfig.holder = holder;
//        return this;
//    }
//
//    /**
//     * Sets custom layout resource for incoming image message.
//     *
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setIncomingImageLayout(@LayoutRes int layout) {
//        this.incomingImageConfig.layout = layout;
//        return this;
//    }
//
//    /**
//     * Sets both of custom view holder class and layout resource for outcoming image message.
//     *
//     * @param holder holder class.
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setOutcomingImageConfig(
//            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder,
//            @LayoutRes int layout) {
//        this.outcomingImageConfig.holder = holder;
//        this.outcomingImageConfig.layout = layout;
//        return this;
//    }
//
//    /**
//     * Sets custom view holder class for outcoming image message.
//     *
//     * @param holder holder class.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setOutcomingImageHolder(
//            @NonNull Class<? extends BaseMessageViewHolder<? extends MessageContentType.Image>> holder) {
//        this.outcomingImageConfig.holder = holder;
//        return this;
//    }
//
//    /**
//     * Sets custom layout resource for outcoming image message.
//     *
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setOutcomingImageLayout(@LayoutRes int layout) {
//        this.outcomingImageConfig.layout = layout;
//        return this;
//    }
//
//    /**
//     * Sets both of custom view holder class and layout resource for date header.
//     *
//     * @param holder holder class.
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setDateHeaderConfig(
//            @NonNull Class<? extends ViewHolder<Date>> holder,
//            @LayoutRes int layout) {
//        this.dateHeaderHolder = holder;
//        this.dateHeaderLayout = layout;
//        return this;
//    }
//
//    /**
//     * Sets custom view holder class for date header.
//     *
//     * @param holder holder class.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setDateHeaderHolder(@NonNull Class<? extends ViewHolder<Date>> holder) {
//        this.dateHeaderHolder = holder;
//        return this;
//    }
//
//    /**
//     * Sets custom layout reource for date header.
//     *
//     * @param layout layout resource.
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public MessageHolders setDateHeaderLayout(@LayoutRes int layout) {
//        this.dateHeaderLayout = layout;
//        return this;
//    }
//
//    /**
//     * Registers custom content type (e.g. multimedia, events etc.)
//     *
//     * @param type            unique id for content type
//     * @param holder          holder class for incoming and outcoming messages
//     * @param incomingLayout  layout resource for incoming message
//     * @param outcomingLayout layout resource for outcoming message
//     * @param contentChecker  {@link ContentChecker} for registered type
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public <TYPE extends MessageContentType>
//    MessageHolders registerContentType(
//            byte type, @NonNull Class<? extends BaseMessageViewHolder<TYPE>> holder,
//            @LayoutRes int incomingLayout,
//            @LayoutRes int outcomingLayout,
//            @NonNull ContentChecker contentChecker) {
//
//        return registerContentType(type,
//                holder, incomingLayout,
//                holder, outcomingLayout,
//                contentChecker);
//    }
//
//    /**
//     * Registers custom content type (e.g. multimedia, events etc.)
//     *
//     * @param type            unique id for content type
//     * @param incomingHolder  holder class for incoming message
//     * @param outcomingHolder holder class for outcoming message
//     * @param incomingLayout  layout resource for incoming message
//     * @param outcomingLayout layout resource for outcoming message
//     * @param contentChecker  {@link ContentChecker} for registered type
//     * @return {@link MessageHolders} for subsequent configuration.
//     */
//    public <TYPE extends MessageContentType>
//    MessageHolders registerContentType(
//            byte type,
//            @NonNull Class<? extends BaseMessageViewHolder<TYPE>> incomingHolder, @LayoutRes int incomingLayout,
//            @NonNull Class<? extends BaseMessageViewHolder<TYPE>> outcomingHolder, @LayoutRes int outcomingLayout,
//            @NonNull ContentChecker contentChecker) {
//
//        if (type == 0)
//            throw new IllegalArgumentException("content type must be greater or less than '0'!");
//
//        customContentTypes.add(
//                new ContentTypeConfig<>(type,
//                        new HolderConfig<>(incomingHolder, incomingLayout),
//                        new HolderConfig<>(outcomingHolder, outcomingLayout)));
//        this.contentChecker = contentChecker;
//        return this;
//    }
//
//    /*
//    * INTERFACES
//    * */
//
//    /**
//     * The interface, which contains logic for checking the availability of content.
//     */
//    public interface ContentChecker<MESSAGE extends IMessage> {
//
//        /**
//         * Checks the availability of content.
//         *
//         * @param message current message in list.
//         * @param type    content type, for which content availability is determined.
//         * @return weather the message has content for the current message.
//         */
//        boolean hasContentFor(MESSAGE message, byte type);
//    }
//
//    /*
//    * PRIVATE METHODS
//    * */
//
//    protected ViewHolder getHolder(ViewGroup parent, int viewType, MessagesListStyle messagesListStyle) {
//        switch (viewType) {
//            case VIEW_TYPE_DATE_HEADER:
//                return getHolder(parent, dateHeaderLayout, dateHeaderHolder, messagesListStyle);
//            case VIEW_TYPE_TEXT_MESSAGE:
//                return getHolder(parent, incomingTextConfig, messagesListStyle);
//            case -VIEW_TYPE_TEXT_MESSAGE:
//                return getHolder(parent, outcomingTextConfig, messagesListStyle);
//            case VIEW_TYPE_IMAGE_MESSAGE:
//                return getHolder(parent, incomingImageConfig, messagesListStyle);
//            case -VIEW_TYPE_IMAGE_MESSAGE:
//                return getHolder(parent, outcomingImageConfig, messagesListStyle);
//            case MessageType.VIEW_TYPE_TYPING_MESSAGE_CHAT_CAMP:
//                return getHolder(parent, incomingTypingConfig, messagesListStyle);
//            case MessageType.VIEW_TYPE_TEXT_MESSAGE_CHATCAMP:
//                return getHolder(parent, incomingTextConfigChatCamp, messagesListStyle);
//            case -MessageType.VIEW_TYPE_TEXT_MESSAGE_CHATCAMP:
//                return getHolder(parent, outcomingTextConfigChatCamp, messagesListStyle);
//            case MessageType.VIEW_TYPE_IMAGE_MESSAGE_CHATCAMP:
//                return getHolder(parent, incomingImageConfigChatCamp, messagesListStyle);
//            case -MessageType.VIEW_TYPE_IMAGE_MESSAGE_CHATCAMP:
//                return getHolder(parent, outcomingImageConfigChatCamp, messagesListStyle);
//            case MessageType.VIEW_TYPE_ACTION_MESSAGE_CHATCAMP:
//                return getHolder(parent, incomingActionConfigChatCamp, messagesListStyle);
//            case -MessageType.VIEW_TYPE_ACTION_MESSAGE_CHATCAMP:
//                return getHolder(parent, outcomingActionConfigChatCamp, messagesListStyle);
//            case MessageType.VIEW_TYPE_VIDEO_MESSAGE_CHATCAMP:
//                return getHolder(parent, incomingVideoConfigChatCamp, messagesListStyle);
//            case -MessageType.VIEW_TYPE_VIDEO_MESSAGE_CHATCAMP:
//                return getHolder(parent, outcomingVideoConfigChatCamp, messagesListStyle);
//            case MessageType.VIEW_TYPE_DOCUMENT_MESSAGE_CHATCAMP:
//                return getHolder(parent, incomingDocumentConfigChatCamp, messagesListStyle);
//            case -MessageType.VIEW_TYPE_DOCUMENT_MESSAGE_CHATCAMP:
//                return getHolder(parent, outcomingDocumentConfigChatCamp, messagesListStyle);
//            default:
//                for (ContentTypeConfig typeConfig : customContentTypes) {
//                    if (Math.abs(typeConfig.type) == Math.abs(viewType)) {
//                        if (viewType > 0)
//                            return getHolder(parent, typeConfig.incomingConfig, messagesListStyle);
//                        else
//                            return getHolder(parent, typeConfig.outcomingConfig, messagesListStyle);
//                    }
//                }
//        }
//        throw new IllegalStateException("Wrong message view type. Please, report this issue on GitHub with full stacktrace in description.");
//    }
//
//    @SuppressWarnings("unchecked")
//    protected void bind(final ViewHolder holder, final Object item, boolean isSelected,
//                        final ImageLoader imageLoader,
//                        final View.OnClickListener onMessageClickListener,
//                        final View.OnLongClickListener onMessageLongClickListener,
//                        final DateFormatter.Formatter dateHeadersFormatter,
//                        final SparseArray<MessagesListAdapter.OnMessageViewClickListener> clickListenersArray) {
//
//        if (item instanceof IMessage) {
//            ((MessageHolders.BaseMessageViewHolder) holder).isSelected = isSelected;
//            ((MessageHolders.BaseMessageViewHolder) holder).imageLoader = imageLoader;
//            holder.itemView.setOnLongClickListener(onMessageLongClickListener);
//            holder.itemView.setOnClickListener(onMessageClickListener);
//
//            for (int i = 0; i < clickListenersArray.size(); i++) {
//                final int key = clickListenersArray.keyAt(i);
//                final View view = holder.itemView.findViewById(key);
//                if (view != null) {
//                    view.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            clickListenersArray.get(key).onMessageViewClick(view, (IMessage) item);
//                        }
//                    });
//                }
//            }
//        } else if (item instanceof Date) {
//            ((MessageHolders.DefaultDateHeaderViewHolder) holder).dateHeadersFormatter = dateHeadersFormatter;
//        }
//
//        holder.onBind(item);
//    }
//
//
//    protected int getViewType(Object item, String senderId) {
//        boolean isOutcoming = false;
//        int viewType;
//
//        if (item instanceof IMessage) {
//            IMessage message = (IMessage) item;
//            isOutcoming = message.getUser().getId().contentEquals(senderId);
//            viewType = getContentViewType(message);
//
//        } else viewType = VIEW_TYPE_DATE_HEADER;
//
//        return isOutcoming ? viewType * -1 : viewType;
//    }
//
//    private ViewHolder getHolder(ViewGroup parent, HolderConfig holderConfig, MessagesListStyle style) {
//        return getHolder(parent, holderConfig.layout, holderConfig.holder, style);
//    }
//
//    private <HOLDER extends ViewHolder>
//    ViewHolder getHolder(ViewGroup parent, @LayoutRes int layout, Class<HOLDER> holderClass, MessagesListStyle style) {
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
//        try {
//            Constructor<HOLDER> constructor = holderClass.getDeclaredConstructor(View.class);
//            constructor.setAccessible(true);
//            HOLDER holder = constructor.newInstance(v);
//            if (holder instanceof DefaultMessageViewHolder && style != null) {
//                ((DefaultMessageViewHolder) holder).applyStyle(style);
//            }
//            return holder;
//        } catch (Exception e) {
//            throw new UnsupportedOperationException("Somehow we couldn't create the ViewHolder for message. Please, report this issue on GitHub with full stacktrace in description.", e);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private short getContentViewType(IMessage message) {
//        if (message.getMessageType() != 0) {
//            return (short) message.getMessageType();
//        }
//        if (message instanceof MessageContentType.Image
//                && ((MessageContentType.Image) message).getImageUrl() != null) {
//            return VIEW_TYPE_IMAGE_MESSAGE;
//        }
//
//        // other default types will be here
//
//        if (message instanceof MessageContentType) {
//            for (int i = 0; i < customContentTypes.size(); i++) {
//                ContentTypeConfig config = customContentTypes.get(i);
//                if (contentChecker == null) {
//                    throw new IllegalArgumentException("ContentChecker cannot be null when using custom content types!");
//                }
//                boolean hasContent = contentChecker.hasContentFor(message, config.type);
//                if (hasContent) return config.type;
//            }
//        }
//
//        return VIEW_TYPE_TEXT_MESSAGE;
//    }
//
//    /*
//    * HOLDERS
//    * */
//
//    /**
//     * The base class for view holders for incoming and outcoming message.
//     * You can extend it to create your own holder in conjuction with custom layout or even using default layout.
//     */
//    public static abstract class BaseMessageViewHolder<MESSAGE extends IMessage> extends ViewHolder<MESSAGE> {
//
//        boolean isSelected;
//
//        /**
//         * Callback for implementing images loading in message list
//         */
//        protected ImageLoader imageLoader;
//
//        public BaseMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        /**
//         * Returns whether is item selected
//         *
//         * @return weather is item selected.
//         */
//        public boolean isSelected() {
//            return isSelected;
//        }
//
//        /**
//         * Returns weather is selection mode enabled
//         *
//         * @return weather is selection mode enabled.
//         */
//        public boolean isSelectionModeEnabled() {
//            return MessagesListAdapter.isSelectionModeEnabled;
//        }
//
//        /**
//         * Getter for {@link #imageLoader}
//         *
//         * @return image loader interface.
//         */
//        public ImageLoader getImageLoader() {
//            return imageLoader;
//        }
//
//        protected void configureLinksBehavior(final TextView text) {
//            text.setLinksClickable(false);
//            text.setMovementMethod(new LinkMovementMethod() {
//                @Override
//                public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
//                    boolean result = false;
//                    if (!MessagesListAdapter.isSelectionModeEnabled) {
//                        result = super.onTouchEvent(widget, buffer, event);
//                    }
//                    itemView.onTouchEvent(event);
//                    return result;
//                }
//            });
//        }
//
//    }
//
//    public static class ChatCampIncomingTypingMessageViewHolder<MESSAGE extends IMessage>
//            extends IncomingTextMessageViewHolder<MESSAGE> {
//
//        TextView usernameTv;
//        AVLoadingIndicatorView indicator;
//
//        public ChatCampIncomingTypingMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            indicator = itemView.findViewById(R.id.indication);
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            usernameTv.setText(message.getUser().getName());
//            indicator.show();
//        }
//    }
//
//
//    public static class ChatCampIncomingActionMessageViewHolder<MESSAGE extends IMessage>
//            extends IncomingTextMessageViewHolder<MESSAGE> {
//        private final TextView usernameTv;
//        private final RecyclerView recyclerView;
//        private final CardView cardView;
//        private final FrameLayout listContainer;
//        private List<IActionSubContent> actionSubContents = new ArrayList<>();
//
//        public ChatCampIncomingActionMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            recyclerView = itemView.findViewById(R.id.recycler_view);
//            cardView = itemView.findViewById(R.id.cv_action_container);
//            listContainer = itemView.findViewById(R.id.fl_list_container);
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            final IActionMessage actionMessage = message.getActionMessage();
//            if (actionMessage != null) {
//                usernameTv.setText(message.getUser().getName());
//                List<IActionContent> actionContents = actionMessage.getActionContents();
//                if (actionContents != null && actionContents.size() > 0) {
//                    if (actionContents.size() > 1) {
//                        // show list layout
//                        listContainer.setVisibility(View.VISIBLE);
//                        cardView.setVisibility(View.GONE);
//                        if (recyclerView != null) {
//                            listContainer.setVisibility(View.VISIBLE);
//                            cardView.setVisibility(View.GONE);
//                            RecyclerView.LayoutManager manager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
//                            recyclerView.setLayoutManager(manager);
//                            ActionListAdapter adapter = new ActionListAdapter(actionContents);
//                            recyclerView.setAdapter(adapter);
//                        }
//
//                    } else {
//                        listContainer.setVisibility(View.GONE);
//                        cardView.setVisibility(View.VISIBLE);
//                        IActionContent actionContent = actionContents.get(0);
//                        populateActionContent(actionContent, cardView, actionSubContents);
//                        // show single card view
//
//                    }
//                }
//            }
//
//        }
//
//        public void populateActionContent(IActionContent actionContent, ViewGroup cardView, List<IActionSubContent> actionSubContent) {
//            cardView.removeAllViews();
//            if (actionContent != null) {
//                View actionContentView = LayoutInflater.from(cardView.getContext()).inflate(R.layout.layout_action_content, cardView, false);
//                ImageView actionContentImage = actionContentView.findViewById(R.id.iv_action_content_image);
//                TextView actionContentTitle = actionContentView.findViewById(R.id.tv_action_content_title);
//                View actionTitleDivider = actionContentView.findViewById(R.id.action_content_title_divider);
//                LinearLayout actionSubcontentContainer = actionContentView.findViewById(R.id.ll_action_subcontent_container);
//                LinearLayout actionActionContainer = actionContentView.findViewById(R.id.ll_action_action_container);
//                if (TextUtils.isEmpty(actionContent.getTitle())) {
//                    actionContentTitle.setVisibility(View.GONE);
//                } else {
//                    actionContentTitle.setVisibility(View.VISIBLE);
//                    Spanned sp = Html.fromHtml(actionContent.getTitle());
//                    actionContentTitle.setText(sp);
//                }
//                if (TextUtils.isEmpty(actionContent.getImageUrl())) {
//                    actionContentImage.setVisibility(View.GONE);
//                    actionContentTitle.setTextColor(ContextCompat.getColor(actionContentView.getContext(), R.color.black));
//                } else {
//                    actionContentImage.setVisibility(View.VISIBLE);
//                    imageLoader.loadImageWithPlaceholder(actionContentImage, actionContent.getImageUrl());
//                }
//                if (actionContentTitle.getVisibility() == View.VISIBLE && actionContentImage.getVisibility() == View.GONE) {
//                    actionTitleDivider.setVisibility(View.VISIBLE);
//                } else {
//                    actionTitleDivider.setVisibility(View.GONE);
//                }
//
//                populateSubContent(actionContent, actionSubcontentContainer, actionSubContent);
//                populateActions(actionContent, actionActionContainer, actionSubContent);
//                cardView.addView(actionContentView);
//            }
//
//        }
//
//        private void populateActions(final IActionContent actionContent, LinearLayout actionActionContainer, final List<IActionSubContent> actionSubContents) {
//            List<String> actionActionList = actionContent.getActions();
//            if (actionActionList != null && actionActionList.size() > 0) {
//                for (int i = 0; i < actionActionList.size(); ++i) {
//                    if (!TextUtils.isEmpty(actionActionList.get(i))) {
//                        LinearLayout actionLayout = (LinearLayout) LayoutInflater.from(actionActionContainer.getContext())
//                                .inflate(R.layout.layout_action_content_action, actionActionContainer, false);
//                        TextView actionName = actionLayout.findViewById(R.id.tv_action_name);
//                        Spanned sp = Html.fromHtml(actionActionList.get(i));
//                        actionName.setText(sp);
//                        actionLayout.setTag(actionActionList.get(i));
//                        actionLayout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (v.getTag() != null && v.getTag() instanceof String) {
//                                    String action = (String) v.getTag();
//                                    List<String> actions = new ArrayList<>();
//                                    actions.add(action);
//                                    actionContent.setActions(actions);
//                                    actionContent.setContents(actionSubContents);
//                                    if (onActionItemClickedListener != null) {
//                                        onActionItemClickedListener.onActionContentActionClicked(actionContent);
//                                        actionSubContents.clear();
//                                    }
//
//                                }
//                            }
//                        });
//                        actionActionContainer.addView(actionLayout);
//                    }
//                }
//            }
//
//        }
//
//        private void populateSubContent(IActionContent actionContent, LinearLayout actionSubcontentContainer, List<IActionSubContent> actionSubContent) {
//            List<IActionSubContent> subContents = actionContent.getContents();
//            if (subContents != null && subContents.size() > 0) {
//                for (int i = 0; i < subContents.size(); ++i) {
//                    IActionSubContent subContent = subContents.get(i);
//                    if (subContent != null) {
//                        LinearLayout subContentLayout = (LinearLayout) LayoutInflater.from(actionSubcontentContainer.getContext())
//                                .inflate(R.layout.layout_action_subcontent, actionSubcontentContainer, false);
//                        ImageView subcontentIv = subContentLayout.findViewById(R.id.iv_action_subcontent_image);
//                        TextView subcontentHeadingTv = subContentLayout.findViewById(R.id.tv_action_subcontent_heading);
//                        LinearLayout subContentContentContainer = subContentLayout.findViewById(R.id.ll_action_subcontent_content_container);
//                        FlowLayout subContentActionContainer = subContentLayout.findViewById(R.id.ll_action_subcontent_action_container);
//                        if (!TextUtils.isEmpty(subContent.getHeading())) {
//                            subcontentHeadingTv.setVisibility(View.VISIBLE);
//                            Spanned sp = Html.fromHtml(subContent.getHeading());
//                            subcontentHeadingTv.setText(sp);
//                        } else {
//                            subcontentHeadingTv.setVisibility(View.GONE);
//                        }
//
//                        if (!TextUtils.isEmpty(subContent.getImageUrl())) {
//                            subcontentIv.setVisibility(View.VISIBLE);
//                            imageLoader.loadImageWithPlaceholder(subcontentIv, subContent.getImageUrl());
//                        } else {
//                            subcontentIv.setVisibility(View.GONE);
//                        }
//
//                        populateSubContentContent(subContent, subContentContentContainer);
//                        populateSubContentActions(subContent, subContentActionContainer, actionSubContent);
//                        actionSubcontentContainer.addView(subContentLayout);
//                    }
//                }
//            }
//        }
//
//        private void populateSubContentContent(IActionSubContent subContent, LinearLayout subContentContentContainer) {
//            List<String> subContentContentList = subContent.getContents();
//            if (subContentContentList != null && subContentContentList.size() > 0) {
//                for (int i = 0; i < subContentContentList.size(); ++i) {
//                    String content = subContentContentList.get(i);
//                    if (!TextUtils.isEmpty(content)) {
//                        LinearLayout actionSubcontentContent = (LinearLayout) LayoutInflater.from(subContentContentContainer.getContext())
//                                .inflate(R.layout.layout_action_subcontent_content, subContentContentContainer, false);
//                        TextView actionSubcontentContentTv = actionSubcontentContent.findViewById(R.id.tv_action_subcontent_content);
//                        Spanned sp = Html.fromHtml(content);
//                        actionSubcontentContentTv.setText(sp);
//                        subContentContentContainer.addView(actionSubcontentContent);
//                    }
//                }
//            }
//        }
//
//        private static void populateSubContentActions(final IActionSubContent subContent, FlowLayout subContentActionContainer, final List<IActionSubContent> actionSubContent) {
//            final List<String> subcontentActionList = subContent.getActions();
//            if (subcontentActionList != null && subcontentActionList.size() > 0) {
//                for (int i = 0; i < subcontentActionList.size(); ++i) {
//                    String action = subcontentActionList.get(i);
//                    LinearLayout actionSubcontentContent = (LinearLayout) LayoutInflater.from(subContentActionContainer.getContext())
//                            .inflate(R.layout.layout_action_subcontent_action, subContentActionContainer, false);
//                    TextView actionSubcontentContentTv = actionSubcontentContent.findViewById(R.id.tv_action_subcontent_action);
//                    String[] actionArray = action.split(",");
//                    action = actionArray[0];
//                    if (actionArray.length > 1) {
//                        String color = actionArray[1];
//                        if (color.trim().equals("null")) {
//                            actionSubcontentContentTv.setBackgroundColor(ContextCompat.getColor(actionSubcontentContent.getContext(), R.color.transparent));
//                            actionSubcontentContentTv.setTextColor(ContextCompat.getColor(actionSubcontentContent.getContext(), R.color.black));
//                            actionSubcontentContentTv.setEnabled(false);
//                            actionSubcontentContentTv.setClickable(false);
//                        } else {
//                            actionSubcontentContentTv.getBackground().mutate().setTint(Color.parseColor(color));
//                            actionSubcontentContentTv.setEnabled(false);
//                            actionSubcontentContentTv.setClickable(false);
//                        }
//                    } else {
//                        if (actionSubcontentContentTv.isSelected()) {
//                            actionSubcontentContentTv.setBackgroundResource(R.drawable.subcontent_action_selected);
//                        } else {
//                            actionSubcontentContentTv.setBackgroundResource(R.drawable.subcontent_action);
//
//                        }
//                    }
//
//                    Spanned sp = Html.fromHtml(action);
//                    actionSubcontentContentTv.setText(sp);
//                    actionSubcontentContentTv.setTag(action);
//                    actionSubcontentContentTv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null && v.getTag() instanceof String) {
//                                String action = (String) v.getTag();
//                                boolean alreadyPresent = false;
//                                for (IActionSubContent subContent1 : actionSubContent) {
//                                    if (subContent1.getId().equals(subContent.getId())) {
//                                        if (v.isSelected()) {
//                                            v.setSelected(false);
//                                            v.setBackgroundResource(R.drawable.subcontent_action);
//                                            subContent1.getActions().remove(action);
//                                        } else {
//                                            v.setSelected(true);
//                                            subContent1.getActions().add(action);
//                                            v.setBackgroundResource(R.drawable.subcontent_action_selected);
//                                        }
//                                        alreadyPresent = true;
//                                        break;
//                                    }
//                                }
//                                if (!alreadyPresent) {
//                                    subContent.getActions().clear();
//                                    subContent.getActions().add(action);
//                                    actionSubContent.add(subContent);
//                                    v.setSelected(true);
//                                    v.setBackgroundResource(R.drawable.subcontent_action_selected);
//                                }
//                            }
//                        }
//                    });
//                    subContentActionContainer.addView(actionSubcontentContent);
//                }
//            }
//        }
//
//
//        public class ActionListAdapter extends RecyclerView.Adapter {
//
//            private static final int VIEW_TYPE_EMPTY = 0;
//            private static final int VIEW_TYPE_ITEM = 1;
//
//            List<IActionContent> list;
//
//            public ActionListAdapter(List<IActionContent> objects) {
//                list = objects;
//            }
//
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                if (viewType == VIEW_TYPE_EMPTY) {
//                    return new EmptyViewHolder(LayoutInflater.from(parent.getContext())
//                            .inflate(R.layout.layout_action_list_empty_item, parent, false));
//                } else {
//                    return new ItemViewHolder(LayoutInflater.from(parent.getContext())
//                            .inflate(R.layout.layout_action_list_item, parent, false));
//                }
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//                if (getItemViewType(position) == VIEW_TYPE_EMPTY) {
//                    ((EmptyViewHolder) holder).bind();
//                } else {
//                    ((ItemViewHolder) holder).bind(list.get(position - 1));
//                }
//            }
//
//            @Override
//            public int getItemCount() {
//                return list.size() + 2;
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                if (position == 0 || position == getItemCount() - 1) {
//                    return VIEW_TYPE_EMPTY;
//                } else {
//                    return VIEW_TYPE_ITEM;
//                }
//            }
//
//            public class EmptyViewHolder extends RecyclerView.ViewHolder {
//
//                public EmptyViewHolder(View itemView) {
//                    super(itemView);
//                }
//
//                public void bind() {
//
//                }
//            }
//
//            public class ItemViewHolder extends RecyclerView.ViewHolder {
//                LinearLayout container;
//                List<IActionSubContent> subContentList = new ArrayList<>();
//
//                public ItemViewHolder(View itemView) {
//                    super(itemView);
//                    container = itemView.findViewById(R.id.container);
//                }
//
//                public void bind(IActionContent actionContent) {
//                    populateActionContent(actionContent, container, subContentList);
//                }
//            }
//        }
//    }
//
//    public static class ChatCampOutcomingActionMessageViewHolder<MESSAGE extends IMessage>
//            extends OutcomingTextMessageViewHolder<MESSAGE> {
//
//        //TODO combine the view holder for incoming and outcoming as everythig is same(except the colors of bubble an may be in future name), the layout can be different.
//        // Other wise we have to copy paste a lot of code
//        private final TextView usernameTv;
//        private final RecyclerView recyclerView;
//        private final CardView cardView;
//        private final FrameLayout listContainer;
//        private List<IActionSubContent> actionSubContents = new ArrayList<>();
//
//
//        public ChatCampOutcomingActionMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            recyclerView = itemView.findViewById(R.id.recycler_view);
//            cardView = itemView.findViewById(R.id.cv_action_container);
//            listContainer = itemView.findViewById(R.id.fl_list_container);
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            final IActionMessage actionMessage = message.getActionMessage();
//            if (actionMessage != null) {
//                usernameTv.setText(message.getUser().getName());
//                List<IActionContent> actionContents = actionMessage.getActionContents();
//                if (actionContents != null && actionContents.size() > 0) {
//                    if (actionContents.size() > 1) {
//                        // show list layout
//                        listContainer.setVisibility(View.VISIBLE);
//                        cardView.setVisibility(View.GONE);
//                        if (recyclerView != null) {
//                            listContainer.setVisibility(View.VISIBLE);
//                            cardView.setVisibility(View.GONE);
//                            RecyclerView.LayoutManager manager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
//                            recyclerView.setLayoutManager(manager);
//                            ChatCampOutcomingActionMessageViewHolder.ActionListAdapter adapter = new ChatCampOutcomingActionMessageViewHolder.ActionListAdapter(actionContents);
//                            recyclerView.setAdapter(adapter);
//                        }
//
//                    } else {
//                        listContainer.setVisibility(View.GONE);
//                        cardView.setVisibility(View.VISIBLE);
//                        IActionContent actionContent = actionContents.get(0);
//                        populateActionContent(actionContent, cardView, actionSubContents);
//                        // show single card view
//
//                    }
//                }
//            }
//
//        }
//
//        public void populateActionContent(IActionContent actionContent, ViewGroup cardView, List<IActionSubContent> actionSubContent) {
//            cardView.removeAllViews();
//            if (actionContent != null) {
//                View actionContentView = LayoutInflater.from(cardView.getContext()).inflate(R.layout.layout_action_content, cardView, false);
//                ImageView actionContentImage = actionContentView.findViewById(R.id.iv_action_content_image);
//                TextView actionContentTitle = actionContentView.findViewById(R.id.tv_action_content_title);
//                View actionTitleDivider = actionContentView.findViewById(R.id.action_content_title_divider);
//                LinearLayout actionSubcontentContainer = actionContentView.findViewById(R.id.ll_action_subcontent_container);
//                LinearLayout actionActionContainer = actionContentView.findViewById(R.id.ll_action_action_container);
//                if (TextUtils.isEmpty(actionContent.getTitle())) {
//                    actionContentTitle.setVisibility(View.GONE);
//                } else {
//                    actionContentTitle.setVisibility(View.VISIBLE);
//                    Spanned sp = Html.fromHtml(actionContent.getTitle());
//                    actionContentTitle.setText(sp);
//                }
//                if (TextUtils.isEmpty(actionContent.getImageUrl())) {
//                    actionContentImage.setVisibility(View.GONE);
//                    actionContentTitle.setTextColor(ContextCompat.getColor(actionContentView.getContext(), R.color.black));
//                } else {
//                    actionContentImage.setVisibility(View.VISIBLE);
//                    imageLoader.loadImageWithPlaceholder(actionContentImage, actionContent.getImageUrl());
//                }
//                if (actionContentTitle.getVisibility() == View.VISIBLE && actionContentImage.getVisibility() == View.GONE) {
//                    actionTitleDivider.setVisibility(View.VISIBLE);
//                } else {
//                    actionTitleDivider.setVisibility(View.GONE);
//                }
//
//                populateSubContent(actionContent, actionSubcontentContainer, actionSubContent);
//                populateActions(actionContent, actionActionContainer, actionSubContent);
//                cardView.addView(actionContentView);
//            }
//
//        }
//
//        private void populateActions(final IActionContent actionContent, LinearLayout actionActionContainer, final List<IActionSubContent> actionSubContents) {
//            List<String> actionActionList = actionContent.getActions();
//            if (actionActionList != null && actionActionList.size() > 0) {
//                for (int i = 0; i < actionActionList.size(); ++i) {
//                    if (!TextUtils.isEmpty(actionActionList.get(i))) {
//                        LinearLayout actionLayout = (LinearLayout) LayoutInflater.from(actionActionContainer.getContext())
//                                .inflate(R.layout.layout_action_content_action, actionActionContainer, false);
//                        TextView actionName = actionLayout.findViewById(R.id.tv_action_name);
//                        Spanned sp = Html.fromHtml(actionActionList.get(i));
//                        actionName.setText(sp);
//                        actionLayout.setTag(actionActionList.get(i));
//                        actionLayout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (v.getTag() != null && v.getTag() instanceof String) {
//                                    String action = (String) v.getTag();
//                                    List<String> actions = new ArrayList<>();
//                                    actions.add(action);
//                                    actionContent.setActions(actions);
//                                    actionContent.setContents(actionSubContents);
//                                    if (onActionItemClickedListener != null) {
//                                        onActionItemClickedListener.onActionContentActionClicked(actionContent);
//                                        actionSubContents.clear();
//                                    }
//
//                                }
//                            }
//                        });
//                        actionActionContainer.addView(actionLayout);
//                    }
//                }
//            }
//
//        }
//
//        private void populateSubContent(IActionContent actionContent, LinearLayout actionSubcontentContainer, List<IActionSubContent> actionSubContent) {
//            List<IActionSubContent> subContents = actionContent.getContents();
//            if (subContents != null && subContents.size() > 0) {
//                for (int i = 0; i < subContents.size(); ++i) {
//                    IActionSubContent subContent = subContents.get(i);
//                    if (subContent != null) {
//                        LinearLayout subContentLayout = (LinearLayout) LayoutInflater.from(actionSubcontentContainer.getContext())
//                                .inflate(R.layout.layout_action_subcontent, actionSubcontentContainer, false);
//                        ImageView subcontentIv = subContentLayout.findViewById(R.id.iv_action_subcontent_image);
//                        TextView subcontentHeadingTv = subContentLayout.findViewById(R.id.tv_action_subcontent_heading);
//                        LinearLayout subContentContentContainer = subContentLayout.findViewById(R.id.ll_action_subcontent_content_container);
//                        FlowLayout subContentActionContainer = subContentLayout.findViewById(R.id.ll_action_subcontent_action_container);
//                        if (!TextUtils.isEmpty(subContent.getHeading())) {
//                            subcontentHeadingTv.setVisibility(View.VISIBLE);
//                            Spanned sp = Html.fromHtml(subContent.getHeading());
//                            subcontentHeadingTv.setText(sp);
//                        } else {
//                            subcontentHeadingTv.setVisibility(View.GONE);
//                        }
//
//                        if (!TextUtils.isEmpty(subContent.getImageUrl())) {
//                            subcontentIv.setVisibility(View.VISIBLE);
//                            imageLoader.loadImageWithPlaceholder(subcontentIv, subContent.getImageUrl());
//                        } else {
//                            subcontentIv.setVisibility(View.GONE);
//                        }
//
//                        populateSubContentContent(subContent, subContentContentContainer);
//                        populateSubContentActions(subContent, subContentActionContainer, actionSubContent);
//                        actionSubcontentContainer.addView(subContentLayout);
//                    }
//                }
//            }
//        }
//
//        private void populateSubContentContent(IActionSubContent subContent, LinearLayout subContentContentContainer) {
//            List<String> subContentContentList = subContent.getContents();
//            if (subContentContentList != null && subContentContentList.size() > 0) {
//                for (int i = 0; i < subContentContentList.size(); ++i) {
//                    String content = subContentContentList.get(i);
//                    if (!TextUtils.isEmpty(content)) {
//                        LinearLayout actionSubcontentContent = (LinearLayout) LayoutInflater.from(subContentContentContainer.getContext())
//                                .inflate(R.layout.layout_action_subcontent_content, subContentContentContainer, false);
//                        TextView actionSubcontentContentTv = actionSubcontentContent.findViewById(R.id.tv_action_subcontent_content);
//                        Spanned sp = Html.fromHtml(content);
//                        actionSubcontentContentTv.setText(sp);
//                        subContentContentContainer.addView(actionSubcontentContent);
//                    }
//                }
//            }
//        }
//
//        private static void populateSubContentActions(final IActionSubContent subContent, FlowLayout subContentActionContainer, final List<IActionSubContent> actionSubContent) {
//            final List<String> subcontentActionList = subContent.getActions();
//            if (subcontentActionList != null && subcontentActionList.size() > 0) {
//                for (int i = 0; i < subcontentActionList.size(); ++i) {
//                    String action = subcontentActionList.get(i);
//                    LinearLayout actionSubcontentContent = (LinearLayout) LayoutInflater.from(subContentActionContainer.getContext())
//                            .inflate(R.layout.layout_action_subcontent_action, subContentActionContainer, false);
//                    TextView actionSubcontentContentTv = actionSubcontentContent.findViewById(R.id.tv_action_subcontent_action);
//                    String[] actionArray = action.split(",");
//                    action = actionArray[0];
//                    if (actionArray.length > 1) {
//                        String color = actionArray[1];
//                        if (color.trim().equals("null")) {
//                            actionSubcontentContentTv.setBackgroundColor(ContextCompat.getColor(actionSubcontentContent.getContext(), R.color.transparent));
//                            actionSubcontentContentTv.setTextColor(ContextCompat.getColor(actionSubcontentContent.getContext(), R.color.black));
//                            actionSubcontentContentTv.setEnabled(false);
//                            actionSubcontentContentTv.setClickable(false);
//                        } else {
//                            actionSubcontentContentTv.getBackground().mutate().setTint(Color.parseColor(color));
//                            actionSubcontentContentTv.setEnabled(false);
//                            actionSubcontentContentTv.setClickable(false);
//                        }
//                    } else {
//                        if (actionSubcontentContentTv.isSelected()) {
//                            actionSubcontentContentTv.setBackgroundResource(R.drawable.subcontent_action_selected);
//                        } else {
//                            actionSubcontentContentTv.setBackgroundResource(R.drawable.subcontent_action);
//
//                        }
//                    }
//
//                    Spanned sp = Html.fromHtml(action);
//                    actionSubcontentContentTv.setText(sp);
//                    actionSubcontentContentTv.setTag(action);
//                    actionSubcontentContentTv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (v.getTag() != null && v.getTag() instanceof String) {
//                                String action = (String) v.getTag();
//                                boolean alreadyPresent = false;
//                                for (IActionSubContent subContent1 : actionSubContent) {
//                                    if (subContent1.getId().equals(subContent.getId())) {
//                                        if (v.isSelected()) {
//                                            v.setSelected(false);
//                                            v.setBackgroundResource(R.drawable.subcontent_action);
//                                            subContent1.getActions().remove(action);
//                                        } else {
//                                            v.setSelected(true);
//                                            subContent1.getActions().add(action);
//                                            v.setBackgroundResource(R.drawable.subcontent_action_selected);
//                                        }
//                                        alreadyPresent = true;
//                                        break;
//                                    }
//                                }
//                                if (!alreadyPresent) {
//                                    subContent.getActions().clear();
//                                    subContent.getActions().add(action);
//                                    actionSubContent.add(subContent);
//                                    v.setSelected(true);
//                                    v.setBackgroundResource(R.drawable.subcontent_action_selected);
//                                }
//                            }
//                        }
//                    });
//                    subContentActionContainer.addView(actionSubcontentContent);
//                }
//            }
//        }
//
//
//        public class ActionListAdapter extends RecyclerView.Adapter {
//
//            private static final int VIEW_TYPE_EMPTY = 0;
//            private static final int VIEW_TYPE_ITEM = 1;
//
//            List<IActionContent> list;
//
//            public ActionListAdapter(List<IActionContent> objects) {
//                list = objects;
//            }
//
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                if (viewType == VIEW_TYPE_EMPTY) {
//                    return new EmptyViewHolder(LayoutInflater.from(parent.getContext())
//                            .inflate(R.layout.layout_action_list_empty_item, parent, false));
//                } else {
//                    return new ItemViewHolder(LayoutInflater.from(parent.getContext())
//                            .inflate(R.layout.layout_action_list_item, parent, false));
//                }
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//                if (getItemViewType(position) == VIEW_TYPE_EMPTY) {
//                    ((EmptyViewHolder) holder).bind();
//                } else {
//                    ((ItemViewHolder) holder).bind(list.get(position - 1));
//                }
//            }
//
//            @Override
//            public int getItemCount() {
//                return list.size() + 2;
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                if (position == 0 || position == getItemCount() - 1) {
//                    return VIEW_TYPE_EMPTY;
//                } else {
//                    return VIEW_TYPE_ITEM;
//                }
//            }
//
//            public class EmptyViewHolder extends RecyclerView.ViewHolder {
//
//                public EmptyViewHolder(View itemView) {
//                    super(itemView);
//                }
//
//                public void bind() {
//
//                }
//            }
//
//            public class ItemViewHolder extends RecyclerView.ViewHolder {
//                LinearLayout container;
//                List<IActionSubContent> actionSubContents = new ArrayList<>();
//
//                public ItemViewHolder(View itemView) {
//                    super(itemView);
//                    container = itemView.findViewById(R.id.container);
//                }
//
//                public void bind(IActionContent actionContent) {
//                    populateActionContent(actionContent, container, actionSubContents);
//                }
//            }
//        }
//    }
//
//    public static class ChatCampIncomingTextMessageViewHolder<MESSAGE extends IMessage>
//            extends MessageHolders.IncomingTextMessageViewHolder<MESSAGE> {
//
//        TextView usernameTv;
//        TextView timeTv;
//
//        public ChatCampIncomingTextMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            usernameTv.setText(message.getUser().getName());
//        }
//    }
//
//    public static class ChatCampOutcomingTextMessageViewHolder<MESSAGE extends IMessage>
//            extends MessageHolders.OutcomingTextMessageViewHolder<MESSAGE> {
//
//        TextView usernameTv;
//        ImageView avatarIv;
//        TextView timeTv;
//
//        public ChatCampOutcomingTextMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            avatarIv = itemView.findViewById(R.id.messageUserAvatar);
//            timeTv = itemView.findViewById(R.id.tv_message_time);
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            usernameTv.setText(message.getUser().getName());
//            timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
//            imageLoader.loadImage(avatarIv, message.getUser().getAvatar());
//        }
//    }
//
//    public static class ChatCampIncomingImageMessageViewHolder<MESSAGE extends MessageContentType.Image>
//            extends MessageHolders.BaseIncomingMessageViewHolder<MESSAGE> {
//        TextView timeTv;
//        TextView usernameTv;
//        RoundedImageView roundedImageView;
//
//        public ChatCampIncomingImageMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            timeTv = itemView.findViewById(R.id.tv_message_time);
//            roundedImageView = itemView.findViewById(R.id.image);
//            if (roundedImageView != null && roundedImageView instanceof RoundedImageView) {
//                ((RoundedImageView) roundedImageView).setCorners(
//                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
//                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
//                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
//                        0
//                );
//            }
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
//            usernameTv.setText(message.getUser().getName());
//            imageLoader.loadImageWithPlaceholder(roundedImageView, message.getImageUrl());
//        }
//    }
//
//    public static class ChatCampOutcomingImageMessageViewHolder<MESSAGE extends MessageContentType.Image>
//            extends MessageHolders.BaseOutcomingMessageViewHolder<MESSAGE> {
//
//        TextView usernameTv;
//        ImageView avatarIv;
//        TextView timeTv;
//        RoundedImageView roundedImageView;
//
//        public ChatCampOutcomingImageMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            avatarIv = itemView.findViewById(R.id.messageUserAvatar);
//            timeTv = itemView.findViewById(R.id.tv_message_time);
//            roundedImageView = itemView.findViewById(R.id.image);
//            if (roundedImageView != null && roundedImageView instanceof RoundedImageView) {
//                ((RoundedImageView) roundedImageView).setCorners(
//                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
//                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
//                        0,
//                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius
//                );
//            }
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            usernameTv.setText(message.getUser().getName());
//            timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
//            imageLoader.loadImage(avatarIv, message.getUser().getAvatar());
//            imageLoader.loadImageWithPlaceholder(roundedImageView, message.getImageUrl());
//        }
//    }
//
//    public static class ChatCampIncomingVideoMessageViewHolder<MESSAGE extends MessageContentType.Video>
//            extends MessageHolders.BaseIncomingMessageViewHolder<MESSAGE> {
//        TextView timeTv;
//        TextView usernameTv;
//        ImageView roundedImageView;
//
//        public ChatCampIncomingVideoMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            timeTv = itemView.findViewById(R.id.tv_message_time);
//            roundedImageView = itemView.findViewById(R.id.image);
////            if (roundedImageView != null && roundedImageView instanceof RoundedImageView) {
////                ((RoundedImageView) roundedImageView).setCorners(
////                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
////                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
////                        0,
////                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius
////                );
////            }
//        }
//
//        @Override
//        public void onBind(final MESSAGE message) {
//            super.onBind(message);
//            timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
//            usernameTv.setText(message.getUser().getName());
//            roundedImageView.setImageResource(R.drawable.ic_video_placeholder);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onVideoItemClickedListener != null) {
//                        onVideoItemClickedListener.onVideoItemClicked(message.getVideoUrl());
//                    }
//                }
//            });
//
//        }
//    }
//
//    public static class ChatCampOutcomingVideoMessageViewHolder<MESSAGE extends MessageContentType.Video>
//            extends MessageHolders.BaseOutcomingMessageViewHolder<MESSAGE> {
//
//        TextView usernameTv;
//        ImageView avatarIv;
//        TextView timeTv;
//        ImageView roundedImageView;
//
//        public ChatCampOutcomingVideoMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            avatarIv = itemView.findViewById(R.id.messageUserAvatar);
//            timeTv = itemView.findViewById(R.id.tv_message_time);
//            roundedImageView = itemView.findViewById(R.id.image);
////            if (roundedImageView != null && roundedImageView instanceof RoundedImageView) {
////                ((RoundedImageView) roundedImageView).setCorners(
////                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
////                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
////                        0,
////                        com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius
////                );
////            }
//        }
//
//        @Override
//        public void onBind(final MESSAGE message) {
//            super.onBind(message);
//            usernameTv.setText(message.getUser().getName());
//            timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
//            imageLoader.loadImage(avatarIv, message.getUser().getAvatar());
//            roundedImageView.setImageResource(R.drawable.ic_video_placeholder);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onVideoItemClickedListener != null) {
//                        onVideoItemClickedListener.onVideoItemClicked(message.getVideoUrl());
//                    }
//                }
//            });
//        }
//    }
//
//    public static class ChatCampIncomingDocumentMessageViewHolder<MESSAGE extends MessageContentType.Document>
//            extends MessageHolders.BaseIncomingMessageViewHolder<MESSAGE> {
//        TextView timeTv;
//        TextView usernameTv;
//        RoundedImageView roundedImageView;
//
//        public ChatCampIncomingDocumentMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            timeTv = itemView.findViewById(R.id.tv_message_time);
//            roundedImageView = itemView.findViewById(R.id.image);
//        }
//
//        @Override
//        public void onBind(final MESSAGE message) {
//            super.onBind(message);
//            timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
//            usernameTv.setText(message.getUser().getName());
//            roundedImageView.setImageResource(R.drawable.ic_document_chat);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onDocumentItemClickedListener != null) {
//                        onDocumentItemClickedListener.onDocumentItemClicked(message);
//                    }
//                }
//            });
//
//        }
//    }
//
//    public static class ChatCampOutcomingDocumentMessageViewHolder<MESSAGE extends MessageContentType.Document>
//            extends MessageHolders.BaseOutcomingMessageViewHolder<MESSAGE> {
//
//        TextView usernameTv;
//        ImageView avatarIv;
//        TextView timeTv;
//        RoundedImageView roundedImageView;
//
//        public ChatCampOutcomingDocumentMessageViewHolder(View itemView) {
//            super(itemView);
//            usernameTv = itemView.findViewById(R.id.tv_username);
//            avatarIv = itemView.findViewById(R.id.messageUserAvatar);
//            timeTv = itemView.findViewById(R.id.tv_message_time);
//            roundedImageView = itemView.findViewById(R.id.image);
//        }
//
//        @Override
//        public void onBind(final MESSAGE message) {
//            super.onBind(message);
//            usernameTv.setText(message.getUser().getName());
//            timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
//            imageLoader.loadImage(avatarIv, message.getUser().getAvatar());
//            roundedImageView.setImageResource(R.drawable.ic_document_chat);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onDocumentItemClickedListener != null) {
//                        onDocumentItemClickedListener.onDocumentItemClicked(message);
//                    }
//                }
//            });
//        }
//    }
//
//    /**
//     * Default view holder implementation for incoming text message
//     */
//    public static class IncomingTextMessageViewHolder<MESSAGE extends IMessage>
//            extends BaseIncomingMessageViewHolder<MESSAGE> {
//
//        protected ViewGroup bubble;
//        protected TextView text;
//
//        public IncomingTextMessageViewHolder(View itemView) {
//            super(itemView);
//            bubble = (ViewGroup) itemView.findViewById(R.id.bubble);
//            text = (TextView) itemView.findViewById(R.id.messageText);
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            if (bubble != null) {
//                bubble.setSelected(isSelected());
//            }
//
//            if (text != null) {
//                text.setText(message.getText());
//            }
//        }
//
//        @Override
//        public void applyStyle(MessagesListStyle style) {
//            super.applyStyle(style);
//            if (bubble != null) {
//                bubble.setPadding(style.getIncomingDefaultBubblePaddingLeft(),
//                        style.getIncomingDefaultBubblePaddingTop(),
//                        style.getIncomingDefaultBubblePaddingRight(),
//                        style.getIncomingDefaultBubblePaddingBottom());
//                ViewCompat.setBackground(bubble, style.getIncomingBubbleDrawable());
//            }
//
//            if (text != null) {
//                text.setTextColor(style.getIncomingTextColor());
//                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getIncomingTextSize());
//                text.setTypeface(text.getTypeface(), style.getIncomingTextStyle());
//                text.setAutoLinkMask(style.getTextAutoLinkMask());
//                text.setLinkTextColor(style.getIncomingTextLinkColor());
//                configureLinksBehavior(text);
//            }
//        }
//    }
//
//    /**
//     * Default view holder implementation for outcoming text message
//     */
//    public static class OutcomingTextMessageViewHolder<MESSAGE extends IMessage>
//            extends BaseOutcomingMessageViewHolder<MESSAGE> {
//
//        protected ViewGroup bubble;
//        protected TextView text;
//
//        public OutcomingTextMessageViewHolder(View itemView) {
//            super(itemView);
//            bubble = (ViewGroup) itemView.findViewById(R.id.bubble);
//            text = (TextView) itemView.findViewById(R.id.messageText);
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            if (bubble != null) {
//                bubble.setSelected(isSelected());
//            }
//
//            if (text != null) {
//                text.setText(message.getText());
//            }
//        }
//
//        @Override
//        public final void applyStyle(MessagesListStyle style) {
//            super.applyStyle(style);
//            if (bubble != null) {
//                bubble.setPadding(style.getOutcomingDefaultBubblePaddingLeft(),
//                        style.getOutcomingDefaultBubblePaddingTop(),
//                        style.getOutcomingDefaultBubblePaddingRight(),
//                        style.getOutcomingDefaultBubblePaddingBottom());
//                ViewCompat.setBackground(bubble, style.getOutcomingBubbleDrawable());
//            }
//
//            if (text != null) {
//                text.setTextColor(style.getOutcomingTextColor());
//                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getOutcomingTextSize());
//                text.setTypeface(text.getTypeface(), style.getOutcomingTextStyle());
//                text.setAutoLinkMask(style.getTextAutoLinkMask());
//                text.setLinkTextColor(style.getOutcomingTextLinkColor());
//                configureLinksBehavior(text);
//            }
//        }
//    }
//
//    /**
//     * Default view holder implementation for incoming image message
//     */
//    public static class IncomingImageMessageViewHolder<MESSAGE extends MessageContentType.Image>
//            extends BaseIncomingMessageViewHolder<MESSAGE> {
//
//        protected ImageView image;
//        protected View imageOverlay;
//
//        public IncomingImageMessageViewHolder(View itemView) {
//            super(itemView);
//            image = (ImageView) itemView.findViewById(R.id.image);
//            imageOverlay = itemView.findViewById(R.id.imageOverlay);
//
//            if (image != null && image instanceof RoundedImageView) {
//                ((RoundedImageView) image).setCorners(
//                        R.dimen.message_bubble_corners_radius,
//                        R.dimen.message_bubble_corners_radius,
//                        R.dimen.message_bubble_corners_radius,
//                        0
//                );
//            }
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            if (image != null && imageLoader != null) {
//                imageLoader.loadImage(image, message.getImageUrl());
//            }
//
//            if (imageOverlay != null) {
//                imageOverlay.setSelected(isSelected());
//            }
//        }
//
//        @Override
//        public final void applyStyle(MessagesListStyle style) {
//            super.applyStyle(style);
//            if (time != null) {
//                time.setTextColor(style.getIncomingImageTimeTextColor());
//                time.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getIncomingImageTimeTextSize());
//                time.setTypeface(time.getTypeface(), style.getIncomingImageTimeTextStyle());
//            }
//
//            if (imageOverlay != null) {
//                ViewCompat.setBackground(imageOverlay, style.getIncomingImageOverlayDrawable());
//            }
//        }
//    }
//
//    /**
//     * Default view holder implementation for outcoming image message
//     */
//    public static class OutcomingImageMessageViewHolder<MESSAGE extends MessageContentType.Image>
//            extends BaseOutcomingMessageViewHolder<MESSAGE> {
//
//        protected ImageView image;
//        protected View imageOverlay;
//
//        public OutcomingImageMessageViewHolder(View itemView) {
//            super(itemView);
//            image = (ImageView) itemView.findViewById(R.id.image);
//            imageOverlay = itemView.findViewById(R.id.imageOverlay);
//
//            if (image != null && image instanceof RoundedImageView) {
//                ((RoundedImageView) image).setCorners(
//                        R.dimen.message_bubble_corners_radius,
//                        R.dimen.message_bubble_corners_radius,
//                        0,
//                        R.dimen.message_bubble_corners_radius
//                );
//            }
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            super.onBind(message);
//            if (image != null && imageLoader != null) {
//                imageLoader.loadImage(image, message.getImageUrl());
//            }
//
//            if (imageOverlay != null) {
//                imageOverlay.setSelected(isSelected());
//            }
//        }
//
//        @Override
//        public final void applyStyle(MessagesListStyle style) {
//            super.applyStyle(style);
//            if (time != null) {
//                time.setTextColor(style.getOutcomingImageTimeTextColor());
//                time.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getOutcomingImageTimeTextSize());
//                time.setTypeface(time.getTypeface(), style.getOutcomingImageTimeTextStyle());
//            }
//
//            if (imageOverlay != null) {
//                ViewCompat.setBackground(imageOverlay, style.getOutcomingImageOverlayDrawable());
//            }
//        }
//    }
//
//    /**
//     * Default view holder implementation for date header
//     */
//    public static class DefaultDateHeaderViewHolder extends ViewHolder<Date>
//            implements DefaultMessageViewHolder {
//
//        protected TextView text;
//        protected String dateFormat;
//        protected DateFormatter.Formatter dateHeadersFormatter;
//
//        public DefaultDateHeaderViewHolder(View itemView) {
//            super(itemView);
//            text = (TextView) itemView.findViewById(R.id.messageText);
//        }
//
//        @Override
//        public void onBind(Date date) {
//            if (text != null) {
//                String formattedDate = null;
//                if (dateHeadersFormatter != null) formattedDate = dateHeadersFormatter.format(date);
//                text.setText(formattedDate == null ? DateFormatter.format(date, dateFormat) : formattedDate);
//            }
//        }
//
//        @Override
//        public void applyStyle(MessagesListStyle style) {
//            if (text != null) {
//                text.setTextColor(style.getDateHeaderTextColor());
//                text.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getDateHeaderTextSize());
//                text.setTypeface(text.getTypeface(), style.getDateHeaderTextStyle());
//                text.setPadding(style.getDateHeaderPadding(), style.getDateHeaderPadding(),
//                        style.getDateHeaderPadding(), style.getDateHeaderPadding());
//            }
//            dateFormat = style.getDateHeaderFormat();
//            dateFormat = dateFormat == null ? DateFormatter.Template.STRING_DAY_MONTH_YEAR.get() : dateFormat;
//        }
//    }
//
//    /**
//     * Base view holder for incoming message
//     */
//    public abstract static class BaseIncomingMessageViewHolder<MESSAGE extends IMessage>
//            extends BaseMessageViewHolder<MESSAGE> implements DefaultMessageViewHolder {
//
//        protected TextView time;
//        protected ImageView userAvatar;
//        protected ImageView readIv;
//        protected TextView fileNameTv;
//
//
//        public BaseIncomingMessageViewHolder(View itemView) {
//            super(itemView);
//            time = (TextView) itemView.findViewById(R.id.messageTime);
//            userAvatar = (ImageView) itemView.findViewById(R.id.messageUserAvatar);
//            readIv = (ImageView) itemView.findViewById(R.id.iv_tick);
//            fileNameTv = itemView.findViewById(R.id.tv_file_name);
//            if (userAvatar != null && userAvatar instanceof RoundedImageView) {
//                ((RoundedImageView) userAvatar).setCorners(
//                        com.stfalcon.chatkit.R.dimen.message_avatar_corners_radius,
//                        com.stfalcon.chatkit.R.dimen.message_avatar_corners_radius,
//                        com.stfalcon.chatkit.R.dimen.message_avatar_corners_radius,
//                        com.stfalcon.chatkit.R.dimen.message_avatar_corners_radius
//                );
//            }
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            if (time != null) {
//                time.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
//            }
//            if (readIv != null) {
//                if (lastTimeRead >= message.getCreatedAt().getTime()) {
//                    readIv.setImageResource(R.drawable.double_tick);
//                } else {
//                    readIv.setImageResource(R.drawable.single_tick);
//                }
//            }
//            if (fileNameTv != null) {
//                fileNameTv.setText(message.getFileName());
//            }
//
//            if (userAvatar != null) {
//                boolean isAvatarExists = imageLoader != null;
//
//                userAvatar.setVisibility(isAvatarExists ? View.VISIBLE : View.GONE);
//                if (isAvatarExists) {
//                    imageLoader.loadImage(userAvatar, message.getUser().getAvatar());
//                }
//            }
//        }
//
//        @Override
//        public void applyStyle(MessagesListStyle style) {
//            if (userAvatar != null) {
//                userAvatar.getLayoutParams().width = style.getIncomingAvatarWidth();
//                userAvatar.getLayoutParams().height = style.getIncomingAvatarHeight();
//            }
//
//        }
//    }
//
//    /**
//     * Base view holder for outcoming message
//     */
//    public abstract static class BaseOutcomingMessageViewHolder<MESSAGE extends IMessage>
//            extends BaseMessageViewHolder<MESSAGE> implements DefaultMessageViewHolder {
//
//        protected TextView time;
//        protected ImageView readIv;
//        protected TextView fileNameTv;
//        ImageView avatarIv;
//
//        public BaseOutcomingMessageViewHolder(View itemView) {
//            super(itemView);
//            time = (TextView) itemView.findViewById(R.id.messageTime);
//            readIv = (ImageView) itemView.findViewById(R.id.iv_tick);
//            fileNameTv = itemView.findViewById(R.id.tv_file_name);
//            avatarIv = itemView.findViewById(R.id.messageUserAvatar);
//            if (avatarIv != null && avatarIv instanceof RoundedImageView) {
//                ((RoundedImageView) avatarIv).setCorners(
//                        com.stfalcon.chatkit.R.dimen.message_avatar_corners_radius,
//                        com.stfalcon.chatkit.R.dimen.message_avatar_corners_radius,
//                        com.stfalcon.chatkit.R.dimen.message_avatar_corners_radius,
//                        com.stfalcon.chatkit.R.dimen.message_avatar_corners_radius
//                );
//            }
//        }
//
//        @Override
//        public void onBind(MESSAGE message) {
//            if (time != null) {
//                time.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
//            }
//            if (avatarIv != null) {
//                imageLoader.loadImageWithPlaceholder(avatarIv, message.getUser().getAvatar());
//            }
//            if (readIv != null) {
//                if (lastTimeRead >= message.getCreatedAt().getTime()) {
//                    readIv.setImageResource(R.drawable.double_tick);
//                } else {
//                    readIv.setImageResource(R.drawable.single_tick);
//                }
//            }
//            if (fileNameTv != null) {
//                fileNameTv.setText(message.getFileName());
//            }
//        }
//
//        @Override
//        public void applyStyle(MessagesListStyle style) {
//
//        }
//    }
//
//    /*
//    * DEFAULTS
//    * */
//
//    interface DefaultMessageViewHolder {
//        void applyStyle(MessagesListStyle style);
//    }
//
//    private static class DefaultIncomingTextMessageViewHolder
//            extends IncomingTextMessageViewHolder<IMessage> {
//
//        public DefaultIncomingTextMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class DefaultOutcomingTextMessageViewHolder
//            extends OutcomingTextMessageViewHolder<IMessage> {
//
//        public DefaultOutcomingTextMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class DefaultIncomingImageMessageViewHolder
//            extends IncomingImageMessageViewHolder<MessageContentType.Image> {
//
//        public DefaultIncomingImageMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class DefaultOutcomingImageMessageViewHolder
//            extends OutcomingImageMessageViewHolder<MessageContentType.Image> {
//
//        public DefaultOutcomingImageMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//
//    private static class ChatcampDefaultIncomingTypingMessageViewHolder
//            extends ChatCampIncomingTypingMessageViewHolder<IMessage> {
//
//        public ChatcampDefaultIncomingTypingMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ChatcampDefaultIncomingTextMessageViewHolder
//            extends ChatCampIncomingTextMessageViewHolder<IMessage> {
//
//        public ChatcampDefaultIncomingTextMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ChatcampDefaultOutcomingTextMessageViewHolder
//            extends ChatCampOutcomingTextMessageViewHolder<IMessage> {
//
//        public ChatcampDefaultOutcomingTextMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ChatcampDefaultIncomingActionMessageViewHolder
//            extends ChatCampIncomingActionMessageViewHolder<IMessage> {
//
//        public ChatcampDefaultIncomingActionMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ChatcampDefaultOutcomingActionMessageViewHolder
//            extends ChatCampOutcomingActionMessageViewHolder<IMessage> {
//
//        public ChatcampDefaultOutcomingActionMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ChatcampDefaultIncomingImageMessageViewHolder
//            extends ChatCampIncomingImageMessageViewHolder<MessageContentType.Image> {
//
//        public ChatcampDefaultIncomingImageMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ChatcampDefaultOutcomingImageMessageViewHolder
//            extends ChatCampOutcomingImageMessageViewHolder<MessageContentType.Image> {
//
//        public ChatcampDefaultOutcomingImageMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//
//    private static class ChatcampDefaultOutcomingVideoMessageViewHolder
//            extends ChatCampOutcomingVideoMessageViewHolder<MessageContentType.Video> {
//
//        public ChatcampDefaultOutcomingVideoMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ChatcampDefaultIncomingVideoMessageViewHolder
//            extends ChatCampIncomingVideoMessageViewHolder<MessageContentType.Video> {
//
//        public ChatcampDefaultIncomingVideoMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ChatcampDefaultIncomingDocumentMessageViewHolder
//            extends ChatCampIncomingDocumentMessageViewHolder<MessageContentType.Document> {
//
//        public ChatcampDefaultIncomingDocumentMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ChatcampDefaultOutcomingDocumentMessageViewHolder
//            extends ChatCampOutcomingDocumentMessageViewHolder<MessageContentType.Document> {
//
//        public ChatcampDefaultOutcomingDocumentMessageViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class ContentTypeConfig<TYPE extends MessageContentType> {
//
//        private byte type;
//        private HolderConfig<TYPE> incomingConfig;
//        private HolderConfig<TYPE> outcomingConfig;
//
//        private ContentTypeConfig(
//                byte type, HolderConfig<TYPE> incomingConfig, HolderConfig<TYPE> outcomingConfig) {
//
//            this.type = type;
//            this.incomingConfig = incomingConfig;
//            this.outcomingConfig = outcomingConfig;
//        }
//    }
//
//    private class HolderConfig<T extends IMessage> {
//
//        protected Class<? extends BaseMessageViewHolder<? extends T>> holder;
//        protected int layout;
//
//        HolderConfig(Class<? extends BaseMessageViewHolder<? extends T>> holder, int layout) {
//            this.holder = holder;
//            this.layout = layout;
//        }
//    }
//
//    // This is used for getting thumbnail from video
//    // taking too much time so it is not worth it. We can do it in an async task but that will take time in separate thread
////    private static Bitmap retriveVideoFrameFromVideo(String videoPath)
////            throws Throwable {
////
////        Bitmap bitmap = null;
////        MediaMetadataRetriever mediaMetadataRetriever = null;
////        try {
////            mediaMetadataRetriever = new MediaMetadataRetriever();
////            if (Build.VERSION.SDK_INT >= 14)
////                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
////            else
////                mediaMetadataRetriever.setDataSource(videoPath);
////
////            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
////        } catch (Exception e) {
////            e.printStackTrace();
////            throw new Throwable(
////                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
////                            + e.getMessage());
////
////        } finally {
////            if (mediaMetadataRetriever != null) {
////                mediaMetadataRetriever.release();
////            }
////        }
////        return bitmap;
////    }
//
//
//    public static float dp2Px(float dp) {
//        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
//        return dp * dm.density;
//    }
//
}
