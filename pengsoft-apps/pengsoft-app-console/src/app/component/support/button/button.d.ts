export interface Button {

    name: string;

    authority?: string;

    type?: 'primary' | 'default' | 'link';

    alignRight?: boolean;

    danger?: boolean;

    divider?: boolean;

    width?: number;

    size?: 'large' | 'small' | 'default';

    icon?: string;

    action: (params?: any) => void;

    disabled?: (form?: any) => boolean;

}