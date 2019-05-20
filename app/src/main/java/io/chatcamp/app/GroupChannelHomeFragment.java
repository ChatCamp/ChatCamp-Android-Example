package io.chatcamp.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.chatcamp.sdk.GroupChannelListQuery;

public class GroupChannelHomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public GroupChannelHomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_home, container, false);
        tabLayout = view.findViewById(R.id.group_navigation);
        viewPager = view.findViewById(R.id.view_pager);
        getActivity().setTitle("Group Channel");
        setUpViewPager();
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        GroupChannelListFragment allFragment = new GroupChannelListFragment();
        allFragment.setParticipantState(GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter.PARTICIPANT_STATE_ALL);
        adapter.add(allFragment, "ALL");
        GroupChannelListFragment inviteFragment = new GroupChannelListFragment();
        inviteFragment.setParticipantState(GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter.PARTICIPANT_STATE_INVITED);
        adapter.add(inviteFragment, "INVITE");
        GroupChannelListFragment acceptedFragment = new GroupChannelListFragment();
        acceptedFragment.setParticipantState(GroupChannelListQuery.GroupChannelListQueryParticipantStateFilter.PARTICIPANT_STATE_ACCEPTED);
        adapter.add(acceptedFragment, "ACCEPTED");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<GroupChannelListFragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void add(GroupChannelListFragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
}
